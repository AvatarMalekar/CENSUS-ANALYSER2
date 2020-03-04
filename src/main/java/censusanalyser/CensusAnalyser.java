package censusanalyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

public class CensusAnalyser<E> {

    public int loadIndiaCensusData(String csvFilePath){

        CsvToBean<IndiaCensusCSV> csvToBean;
        try(Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder icsvBuilder=CSVBuilderFactory.createBuilder();
           List<IndiaCensusCSV> censusList = icsvBuilder.getCSVFileList(reader,IndiaCensusCSV.class);
           return censusList.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
        catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        }
    }

    public int loadIndiaStateData(String indiaStateCode) {
        CsvToBean<IndiaStateCodeCSV> csvToBean;
        try(Reader reader = Files.newBufferedReader(Paths.get(indiaStateCode));) {
            ICSVBuilder icsvBuilder=CSVBuilderFactory.createBuilder();
           List<IndiaStateCodeCSV> censusCSVList = icsvBuilder.getCSVFileList(reader,IndiaStateCodeCSV.class);
            return censusCSVList.size();

        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
        catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        }
    }
   private <E>int getCount(Iterator<E> censusCSVIterator) {
        Iterable<E> censusIterator=()-> censusCSVIterator;
        int namOfEateries = (int) StreamSupport.stream(censusIterator.spliterator(),false).count();
        return namOfEateries;
    }

}

