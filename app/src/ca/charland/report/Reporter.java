package ca.charland.report;

import java.awt.Font;

import ooo.connector.BootstrapSocketConnector;

import com.sun.star.awt.FontUnderline;
import com.sun.star.awt.FontWeight;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.container.XIndexAccess;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.Locale;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.table.XColumnRowRange;
import com.sun.star.table.XTableColumns;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.MalformedNumberFormatException;
import com.sun.star.util.NumberFormat;
import com.sun.star.util.XNumberFormats;
import com.sun.star.util.XNumberFormatsSupplier;

public class Reporter {

    private XComponentContext context;
    private XMultiComponentFactory serviceManager;
    private XSpreadsheetDocument document;
    private XSpreadsheet sheet;
    private int percentageFormat;
    private int percentageTwoDigitFormat;
    private int doubleFormat;
    private int dateFormat;
    private int intFormat;

    public Reporter() {
        String oooExeFolder = setLibreOfficeFolder();

        // get the remote office context. If necessary a new office
        // process is started
        try {
            context = BootstrapSocketConnector.bootstrap(oooExeFolder);
        } catch (BootstrapException e) {
            throw new SpreadSheetException(e);
        }

        System.out.println("Connected to a running office ...");
        serviceManager = context.getServiceManager();

        // create a new spreadsheet document
        XComponentLoader aLoader = null;
        try {
            aLoader = (XComponentLoader) UnoRuntime.queryInterface(
                    XComponentLoader.class, serviceManager
                            .createInstanceWithContext(
                                    "com.sun.star.frame.Desktop", context));
        } catch (Exception e) {
            throw new SpreadSheetException(e);
        }

        try {
            document = (XSpreadsheetDocument) UnoRuntime.queryInterface(
                    XSpreadsheetDocument.class, aLoader.loadComponentFromURL(
                            "private:factory/scalc", "_blank", 0,
                            new PropertyValue[0]));
        } catch (Exception e) {
            throw new SpreadSheetException(e);
        }
        initSpreadsheet();
        setKeyFormats();
    }

    private static String setLibreOfficeFolder() {
        String os = System.getProperty("os.name");
        if (os.contains("Linux")) {
            return "/usr/lib/libreoffice/program";
        }
        return "D:/Program Files (x86)/LibreOffice 4/program";
    }

    private void setKeyFormats() {
        // Query the number formats supplier of the spreadsheet document
        XNumberFormatsSupplier numberFormatsSupplier = (XNumberFormatsSupplier) UnoRuntime
                .queryInterface(XNumberFormatsSupplier.class, document);

        // Get the number formats from the supplier
        XNumberFormats numberFormats = numberFormatsSupplier.getNumberFormats();

        // Get the number format index key of the default currency format,
        // note the empty locale for default locale
        Locale aLocale = new Locale();
        try {
            dateFormat = numberFormats.addNew("MMM D", aLocale);
            percentageFormat = numberFormats.addNew("##.0%", aLocale);
            percentageTwoDigitFormat = numberFormats.addNew("##.00%", aLocale);
            doubleFormat = numberFormats.addNew("##.0", aLocale);
            intFormat = numberFormats.addNew("##.#", aLocale);
        } catch (MalformedNumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void initSpreadsheet() {
        XSpreadsheets aSheets = document.getSheets();
        XIndexAccess aSheetsIA = (XIndexAccess) UnoRuntime.queryInterface(
                XIndexAccess.class, aSheets);
        try {
            sheet = (XSpreadsheet) UnoRuntime.queryInterface(
                    XSpreadsheet.class, aSheetsIA.getByIndex(0));
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (WrappedTargetException e) {
            e.printStackTrace();
        }
    }

    public void setDate(String date, int x, int y) {
        setFormula(date, x, y, dateFormat);
    }

    private void setMonth(int x, int y) {
        setFormula(getMonthFormula(y + 1), x, y, NumberFormat.NUMBER);
    }

    private String getMonthFormula(int row) {
        return "=MONTH(A" + row + ")";
    }

    private void setFormula(String formula, int x, int y, int format) {
        XCell xCell = getCell(x, y);
        xCell.setFormula(formula);
        setFormat(x, y, format);
    }

    private void setDouble(String value, int x, int y) {
        setValue(Double.parseDouble(value), x, y, doubleFormat);
    }

    private void setPercentage(String value, int x, int y) {
        setValue(Double.parseDouble(value), x, y, percentageFormat);
    }

    private void setInt(String value, int x, int y) {
        setValue(Integer.parseInt(value), x, y, intFormat);
    }

    XCell setString(String value, int x, int y) {
        XCell xCell = getCell(x, y);
        xCell.setFormula(value);
        return xCell;
    }

    void setBoldedString(String value, int x, int y) throws Exception {
        XCell xCell = setString(value, x, y);
        XPropertySet xPropSet = UnoRuntime.queryInterface(
                com.sun.star.beans.XPropertySet.class, xCell);

        xPropSet.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
    }

    public void setItalizedString(String value, int x, int y) throws Exception {
        XCell xCell = setString(value, x, y);
        XPropertySet xPropSet = UnoRuntime.queryInterface(
                com.sun.star.beans.XPropertySet.class, xCell);

        xPropSet.setPropertyValue("CharPosture", Font.ITALIC);
    }

    public void setUnderlineString(String value, int x, int y) throws Exception {
        XCell xCell = setString(value, x, y);
        XPropertySet xPropSet = UnoRuntime.queryInterface(
                com.sun.star.beans.XPropertySet.class, xCell);
        xPropSet.setPropertyValue("CharUnderline", FontUnderline.SINGLE);
    }

    private void setMuscleMassPercentageOfTotalWeightFormula(int x, int y) {
        setFormula(getMuscleMassPercentageOfTotalWeightFormula(y + 1), x, y,
                percentageTwoDigitFormat);
    }

    private String getMuscleMassPercentageOfTotalWeightFormula(int row) {
        String muscleMass = "O" + row;
        String weight = "C" + row;
        return "=" + muscleMass + "/" + weight;
    }

    private void setValue(double value, int x, int y, int format) {
        XCell xCell = getCell(x, y);
        xCell.setValue(value);
        setFormat(x, y, format);
    }

    private void setFormat(int x, int y, int format) {
        XCell cell = getCell(x, y);
        setNumberFormat(cell, format);
    }

    private XCell getCell(int x, int y) {
        XCell cell = null;
        try {
            cell = sheet.getCellByPosition(x, y);
        } catch (IndexOutOfBoundsException e) {
            throw new SpreadSheetException(e);
        }
        return cell;
    }

    private void setNumberFormat(XCell cell, int format) {

        // Query the property set of the cell range
        XPropertySet xCellProp = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, cell);

        try {
            xCellProp.setPropertyValue("NumberFormat", format);
        } catch (Exception e) {
            throw new SpreadSheetException(e);
        }
    }

    void setWidthsForCalendarDates() throws WrappedTargetException,
            IndexOutOfBoundsException, UnknownPropertyException,
            PropertyVetoException, IllegalArgumentException {

        for (int x = 0; x <= 7 * 3 + 1; x++) {
            setWidth(x, 500);
        }
    }

    void setWidth(int x, int width) throws IndexOutOfBoundsException,
            WrappedTargetException, UnknownPropertyException,
            PropertyVetoException, IllegalArgumentException {
        XColumnRowRange columnRowRange = UnoRuntime.queryInterface(
                com.sun.star.table.XColumnRowRange.class, sheet);
        XTableColumns columns = columnRowRange.getColumns();
        Object columnObj = columns.getByIndex(x);
        XPropertySet xPropSet = UnoRuntime.queryInterface(
                com.sun.star.beans.XPropertySet.class, columnObj);
        xPropSet.setPropertyValue("Width", Integer.valueOf(width));
    }
}