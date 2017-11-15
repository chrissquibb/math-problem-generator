import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class MathProblemGenerator
{
    private Properties props_;

    public MathProblemGenerator()
    {
        props_ = loadProperties();
    }

    public static void main(String args[]) throws IOException
    {

        MathProblemGenerator lMathProblemGenerator = new MathProblemGenerator();
        lMathProblemGenerator.generateBasedOnProps();
    }

    private void generateBasedOnProps()
    {
        int numberOfSheets = Integer.parseInt(props_.getProperty("numberOfSheets"));

        for (int i = 0; i < numberOfSheets; ++i)
        {
            generateSheet(i+1);
        }
    }

    private void generateSheet(int aInSheetNumber)
    {
        System.out.println("Generating sheet #"+aInSheetNumber);
        List<MathProblem> lMathProblems = generateMathProblems();

        String lHeaderText = "Karaté maths - Niveau " + Integer.parseInt(props_.getProperty("level")) + " - #"+aInSheetNumber;
        createPdf(lHeaderText+".pdf", lHeaderText, lMathProblems);
    }

    private List<MathProblem> generateMathProblems()
    {
        List<OperatorType> lOperatorTypes = parseOperatorTypes(props_.getProperty("operationTypes"));

        List<MathProblem> lMathProblems = new ArrayList<>();
        for (int i = 0; i < Integer.parseInt(props_.getProperty("numberPerSheet")); ++i)
        {
            int lFirstOperand = ThreadLocalRandom.current().nextInt(
                    Integer.parseInt(props_.getProperty("firstOperandMin")),
                    Integer.parseInt(props_.getProperty("firstOperandMax")) + 1);
            int lSecondOperand = ThreadLocalRandom.current().nextInt(
                    Integer.parseInt(props_.getProperty("secondOperandMin")),
                    Integer.parseInt(props_.getProperty("secondOperandMax")) + 1);
            OperatorType lOperatorType = lOperatorTypes.get(
                    ThreadLocalRandom.current().nextInt(0, lOperatorTypes.size()));
            if (lOperatorType.equals(OperatorType.MINUS) || lOperatorType.equals(OperatorType.DIVISION))
            {
                if (lSecondOperand > lFirstOperand)
                {
                    int lTmp = lSecondOperand;
                    lSecondOperand = lFirstOperand;
                    lFirstOperand = lTmp;
                }
            }
            lMathProblems.add(new MathProblem(lFirstOperand, lSecondOperand, lOperatorType));
        }

        lMathProblems.forEach(lMathProblem -> System.out.println("Generated: " + lMathProblem));

        return lMathProblems;
    }

    private Properties loadProperties()
    {
        Properties lProperties = new Properties();
        InputStream lInput = null;

        try {

            lInput = new FileInputStream("config.properties");
            lProperties.load(lInput);
            System.out.println("Read the following properties: ");
            System.out.println("\tnumberPerSheet: "+lProperties.getProperty("numberPerSheet"));
            System.out.println("\tnumberOfSheets: "+lProperties.getProperty("numberOfSheets"));
            System.out.println("\toperationTypes: "+lProperties.getProperty("operationTypes"));
            System.out.println("\tfirstOperandMax: "+lProperties.getProperty("firstOperandMax"));
            System.out.println("\tfirstOperandMin: "+lProperties.getProperty("firstOperandMin"));
            System.out.println("\tsecondOperandMax: "+lProperties.getProperty("secondOperandMax"));
            System.out.println("\tsecondOperandMin: "+lProperties.getProperty("secondOperandMin"));

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (lInput != null) {
                try {
                    lInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return lProperties;
    }

    private List<OperatorType> parseOperatorTypes(String aInOperatorTypes)
    {
        // In the form of "+-*/"
        List<OperatorType> lOperatorTypes = new ArrayList<>();
        for (int i = 0; i < aInOperatorTypes.length(); ++i)
        {
            lOperatorTypes.add(OperatorType.convert(String.valueOf(aInOperatorTypes.charAt(i))));
        }

        return lOperatorTypes;
    }

    private void createPdf(String aInDest, String aInHeaderText, List<MathProblem> aInMathProblems)
    {
        try
        {
            PdfWriter lWriter = new PdfWriter(aInDest);
            PdfDocument lPdf = new PdfDocument(lWriter);
            Document lDocument = new Document(lPdf);

            addPdfContent(lDocument, aInHeaderText, aInMathProblems);
            lDocument.add(new Paragraph("\n\n\n"));
            addPdfContent(lDocument, aInHeaderText, aInMathProblems);

            lDocument.close();
        }
        catch (IOException e)
        {
            System.err.println(e.toString());
        }
    }

    private void addPdfContent(Document aInDocument, String aInHeaderText, List<MathProblem> aInMathProblems)
    {
        // Header
        Paragraph lHeader = new Paragraph(aInHeaderText+"\n\n");
        lHeader.setTextAlignment(TextAlignment.CENTER);
        aInDocument.add(lHeader);

        // Table of math problems
        Table lTable = new Table(new float[]{1,1,1,1,1});
        //lTable.setWidthPercent(100);
        aInMathProblems.forEach(lMathProblem -> {
            Cell lCell = new Cell();
            lCell.setBorder(Border.NO_BORDER);
            lCell.setHeight(30);
            lCell.setWidth(100);
            lCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
            Paragraph lContent = new Paragraph(lMathProblem.toString());
            lContent.setTextAlignment(TextAlignment.CENTER);
            lCell.add(lContent);
            lTable.addCell(lCell);
        });

        aInDocument.add(lTable);
    }
}
