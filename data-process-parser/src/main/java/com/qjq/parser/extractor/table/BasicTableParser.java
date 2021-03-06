package com.qjq.parser.extractor.table;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jaxen.Navigator;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.qjq.parser.extractor.html.HtmlPageParser;
import com.qjq.parser.extractor.utils.CommonUtil;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.Tag;
import net.sf.json.JSONArray;

public class BasicTableParser implements TableParser {

    protected List<Table> table;
    protected JSONArray jsonTables;
    protected String html;
    protected String xpathContent;
    protected String filterColsTag[] = { "p", "br", "a" };
    protected Map<String, Element> tableLoc;
    protected String reg = "&\\$\\$\\$#[\\d]*";

    public void processHiddenCode() {
        Source source = new Source(xpathContent);
        List<Element> elements = source.getAllElements(HTMLElementName.DIV);
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            String value = element.getAttributeValue("style");
            if (value != null && value.contains("none")) {
                xpathContent = xpathContent.replace(element.toString(), "");
            }
        }
    }

    public List<Table> process(String html, String attribute, Map<String, String> reg, String xpath) {
        return null;
    }

    protected void processHtmlMergeTable(Element element) {
        List<Element> elements = element.getAllElements(HTMLElementName.TABLE);
        if (elements.size() > 1) {
            tableLoc = new HashMap<String, Element>();
            for (int i = 0; i < elements.size(); i++) {
                Element element2 = elements.get(i);
                List<Element> tables = element2.getAllElements(HTMLElementName.TABLE);
                if (tables != null && tables.size() == 1) {
                    xpathContent = xpathContent.replace(element2.toString(), "&$$$#" + i);
                    tableLoc.put("&$$$#" + i, element2);
                }
            }
        }
    }

    protected Table getTableContent(Element element) {
        Table table = new Table();
        Cell[][] cells = null;
        List<Element> trList = element.getAllElements(HTMLElementName.TR);
        table.setRows(trList.size());
        boolean flag = false;
        for (int rows = 0; rows < trList.size(); rows++) {
            Element trElement = trList.get(rows);
            List<Element> tdList = trElement.getAllElements(HTMLElementName.TD);
            List<Element> thList = trElement.getAllElements(HTMLElementName.TH);
            if (!flag) {
                int c1 = getCols(tdList);
                int c2 = getCols(thList);
                int h1 = getTableHead(tdList);
                int h2 = getTableHead(thList);

                table.setCols(c1 + c2);
                table.setContentRow(h1 + h2);

                cells = new Cell[table.getRows()][table.getCols()];
                for (int i = 0; i < table.getRows(); i++) {
                    for (int j = 0; j < table.getCols(); j++) {
                        cells[i][j] = new Cell();
                        cells[i][j].setText("");
                    }
                }
                flag = true;
            }
            int j = 0;

            for (int td = 0; td < thList.size(); td++) {
                Element element2 = thList.get(td);
                Table table2 = null;
                boolean isConTable = false;
                if (element2.getAllElements(HTMLElementName.TABLE).size() > 0) {
                    isConTable = true;
                }
                if (isConTable) {
                    table2 = getTableContent(element2);
                }
                String content = "";
                if (!isConTable)
                    content = element2.getTextExtractor().toString();
                int clos = getTdColSpan(thList.get(td));
                int rowspan = getTrRowSpan(thList.get(td));
                boolean mark = false;
                for (int t = 0; t < clos && j < table.getCols(); t++) {
                    if (cells[rows][j].getMark() != null && cells[rows][j].getMark().equals("megercell")) {
                        j++;
                        t--;
                        continue;
                    }
                    cells[rows][j].setRows(rows);
                    cells[rows][j].setCols(j - t);
                    if (!mark) {
                        if (!isConTable)
                            cells[rows][j].setText(CommonUtil.removeHeadBlank(content));
                        else
                            cells[rows][j].addCellTable(table2);
                    } else {
                        cells[rows][j].setText("");
                        cells[rows][j].setMark("megercell");
                    }
                    if (rowspan > 1) {
                        for (int i = 1; i < rowspan; i++) {
                            cells[rows + i][j].setRows(rows + i);
                            cells[rows + i][j].setCols(j);
                            cells[rows + i][j].setMark("megercell");
                        }
                    }
                    mark = true;
                    j++;
                }
            }

            for (int td = 0; td < tdList.size(); td++) {
                Element element2 = tdList.get(td);
                Table table2 = null;
                boolean isConTable = false;
                String content = element2.getTextExtractor().toString();
                /*List<Tag> tags = element2.getAllTags();
                if (tags.size() > 2 && rows >= 1 && td == 1) { // 第一行 第一个
                    Tag tag = tags.get(1);
                    content += "@@" + tag.getElement().getAttributeValue("href");
                }*/
                if (CommonUtil.removeHuanHang(content).contains("&$$$#")) {

                    Pattern pattern = Pattern.compile(reg);
                    Matcher matcher = pattern.matcher(content);
                    while (matcher.find()) {

                        String key = matcher.group(0);
                        // System.out.println(key);
                        isConTable = true;
                        table2 = getTableContent(tableLoc.get(key));
                    }

                }

                int clos = getTdColSpan(tdList.get(td));
                int rowspan = getTrRowSpan(tdList.get(td));
                boolean mark = false;
                for (int t = 0; t < clos && j < table.getCols(); t++) {
                    if (cells[rows][j].getMark() != null && cells[rows][j].getMark().equals("megercell")) {
                        j++;
                        t--;
                        continue;
                    }
                    cells[rows][j].setRows(rows);
                    cells[rows][j].setCols(j - t);
                    if (!mark) {
                        if (!isConTable)
                            cells[rows][j].setText(CommonUtil.removeHeadBlank(content));
                        else
                            cells[rows][j].addCellTable(table2);
                    } else {
                        cells[rows][j].setText("");
                        cells[rows][j].setMark("megercell");
                    }
                    if (rowspan > 1) {
                        for (int i = 1; i < rowspan; i++) {
                            cells[rows + i][j].setRows(rows + i);
                            cells[rows + i][j].setCols(j);
                            cells[rows + i][j].setMark("megercell");
                        }
                    }
                    mark = true;
                    j++;
                }
            }

        }
        table.setCells(cells);
        return table;
    }

    protected int getTdColSpan(Element element) {
        String value = element.getAttributeValue("colspan");
        if (value == null || value.equals(""))
            value = "1";
        return Integer.valueOf(value);
    }

    protected int getTrRowSpan(Element element) {
        String value = element.getAttributeValue("rowspan");
        if (value == null || value.equals(""))
            value = "1";
        return Integer.valueOf(value);
    }

    protected int getTableHead(List<Element> list) {
        int tableHead = 1;
        for (int i = 0; i < list.size(); i++) {
            Element tdEl = list.get(i);
            String c = tdEl.getAttributeValue("rowspan");
            if (c != null && !c.equals("")) {
                int tmp = Integer.valueOf(c);
                if (tableHead < tmp)
                    tmp = tableHead;
            }
        }
        return tableHead;
    }

    protected int getCols(List<Element> List) {
        int clos = 0;
        for (int td = 0; td < List.size(); td++) {
            Element tdEl = List.get(td);
            String c = tdEl.getAttributeValue("colspan");
            if (c != null && !c.equals("")) {
                clos += Integer.valueOf(c);
            } else
                clos++;
        }
        return clos;
    }

    protected String getXpathContent(String xpathStr) {
        StringBuilder builder = new StringBuilder();
        Document document = HtmlPageParser.getHtmlPageParser().parser("", html);
        try {
            XPath expression = new DOMXPath(xpathStr);
            if (xpathStr == null || xpathStr.equals(""))
                return "";
            List results = expression.selectNodes(document);
            Iterator iterator = results.iterator();
            Navigator navigator = expression.getNavigator();
            while (iterator.hasNext()) {
                Node result = (Node) iterator.next();
                builder.append(getHtmlByNode(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    protected String getHtmlByNode(Node node) {
        String xmlStr = "";
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.setOutputProperty("encoding", "UTF-8");// 解决中文问题，试过用GBK不行
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            t.transform(new DOMSource(node), new StreamResult(bos));
            xmlStr = bos.toString();
        } catch (Exception e) {
        }
        if (!xmlStr.equals(""))
            xmlStr = xmlStr.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
        return xmlStr;
    }

    protected boolean successByAttribute(Element element, String attribute) {
        String tableTag = element.getStartTag().toString();
        tableTag = CommonUtil.removeHuanHang(tableTag);
        attribute = CommonUtil.removeHuanHang(attribute);
        if (tableTag.equalsIgnoreCase(attribute))
            return true;
        return false;
    }

    public JSONArray getJsonTables() {
        if (table == null)
            return null;
        jsonTables = JSONArray.fromObject(table);
        return jsonTables;
    }

    public List<Table> process(String html, String attraibute, String xpath, Map<String, String> rowtag,
            Map<String, String> colreg) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Table> process(String html, String attraibute, String xpath, String rowtag, String colreg) {
        // TODO Auto-generated method stub
        return null;
    }
}
