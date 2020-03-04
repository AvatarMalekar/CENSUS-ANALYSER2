package censusanalyser;

public class CensusAnalyserException extends RuntimeException {

    public CensusAnalyserException(ExceptionType type) {
        this.type=type;
    }

    enum ExceptionType {
        CENSUS_FILE_PROBLEM,UNABLE_TO_PARSE,NO_CENSUS_DATA ;
    }

    ExceptionType type;

    public CensusAnalyserException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }

    public CensusAnalyserException(String message, ExceptionType type, Throwable cause) {
        super(message, cause);
        this.type = type;
    }
}
