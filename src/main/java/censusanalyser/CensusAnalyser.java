package censusanalyser;

import com.google.gson.Gson;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

import static jdk.nashorn.internal.objects.NativeArray.sort;

public class CensusAnalyser<E> {
    List<IndiaCensusCSV> censusList=null;
    public int loadIndiaCensusData(String csvFilePath){

        CsvToBean<IndiaCensusCSV> csvToBean;
        try(Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder icsvBuilder=CSVBuilderFactory.createBuilder();
          censusList = icsvBuilder.getCSVFileList(reader,IndiaCensusCSV.class);
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

    public String getStateWiseSortCensusData() {
            if(censusList.size()==0 || censusList==null){
                throw new CensusAnalyserException(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
            }
            Comparator<IndiaCensusCSV> censusCSVComparator=Comparator.comparing(StateCensus->StateCensus.state);
            this.sort(censusCSVComparator);
            String json=new Gson().toJson(censusList);
            return json;

    }

    private void sort(Comparator<IndiaCensusCSV> censusCSVComparator) {
        for(int i=0;i<censusList.size()-1;i++){
            for(int j=0;j<censusList.size()-i-1;j++){
                IndiaCensusCSV census1=censusList.get(j);
                IndiaCensusCSV census2=censusList.get(j+1);
                if(censusCSVComparator.compare(census1,census2)>0){
                    censusList.set(j,census2);
                    censusList.set(j+1,census1);
                }
            }
        }
    }
}

