package censusanalyser;

public class CSVBuilderException extends RuntimeException {


    enum ExceptionType {
       UNABLE_TO_PARSE_EXCEPTION;
    }

    ExceptionType type;
    public CSVBuilderException(String message, ExceptionType unableToParseException) {
        super(message);
        this.type=type;
    }
}
