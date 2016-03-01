package com.oxygenxml.validation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.saxonica.ee.jaxp.SchemaFactoryImpl;

import net.sf.saxon.lib.FeatureKeys;
import net.sf.saxon.lib.Validation;

public class SchemaValidation {
    /**
     * Error handler for collecting Saxon validation errors.
     */
    private static ErrorHandler errorHandler = new ErrorHandler() {
        @Override
        public void warning(SAXParseException we) throws SAXException {
            System.out.println("Warning: " + we);
        }
        @Override
        public void fatalError(SAXParseException fe) throws SAXException {
            System.out.println("Fatal error: " + fe);
        }
        @Override
        public void error(SAXParseException ee) throws SAXException {
            System.out.println("Error: " + ee);
        }
    };

    public static void main(String[] args) throws SAXException, FileNotFoundException, IOException {
        File schemaFile = new File("samples/validation/personal.xsd");
        File xmlFile = new File("samples/validation/personal-schema.xml");

        SchemaFactoryImpl schemaFactory = new com.saxonica.ee.jaxp.SchemaFactoryImpl();

        // Set error handler
        schemaFactory.setErrorHandler(errorHandler);

        // Strict schema validation
        schemaFactory.setProperty(FeatureKeys.SCHEMA_VALIDATION, Validation.STRICT);
        Schema schema = schemaFactory.newSchema(schemaFile);
        Validator newValidator = schema.newValidator();
        newValidator.setErrorHandler(errorHandler);
        newValidator.validate(new SAXSource(new InputSource(new FileInputStream(xmlFile))));
    }
}
