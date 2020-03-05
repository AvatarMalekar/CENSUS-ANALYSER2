package censusanalyser;

import com.google.gson.Gson;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static jdk.nashorn.internal.objects.NativeArray.sort;

public class CensusAnalyser<E> {
    List<IndiaCensusDTO> censusList=null;
    Map<String,IndiaCensusDTO> censusMap=null;

    public CensusAnalyser() {
        censusList=new ArrayList<>();
        censusMap=new HashMap<>();
    }

    public int loadIndiaCensusData(String csvFilePath){

        CsvToBean<IndiaCensusCSV> csvToBean;
        try(Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder icsvBuilder=CSVBuilderFactory.createBuilder();
            Iterator<IndiaCensusCSV> stateCensus=icsvBuilder.getCSVIterator(reader,IndiaCensusCSV.class);
            while(stateCensus.hasNext()){
                IndiaCensusDTO tempObj=new IndiaCensusDTO(stateCensus.next());
                this.censusMap.put(tempObj.state,tempObj);
            }
            censusList=censusMap.values().stream().collect(Collectors.toList());
           return censusMap.size();
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
           Iterator<IndiaStateCodeCSV> censusCSVIterator=icsvBuilder.getCSVIterator(reader,IndiaStateCodeCSV.class);
           while(censusCSVIterator.hasNext()){
                IndiaStateCodeCSV code=censusCSVIterator.next();
                IndiaCensusDTO csv=censusMap.get(code.stateCode);
                if(csv==null)
                    continue;
                else
                    csv.stateCode=code.stateCode;
            }
            censusList=censusMap.values().stream().collect(Collectors.toList());
            return censusMap.size();

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
            if(censusMap.size()==0 || censusMap==null){
                throw new CensusAnalyserException(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
            }
            Comparator<IndiaCensusDTO> censusCSVComparator=Comparator.comparing(StateCensus->StateCensus.state);
            this.sort(censusCSVComparator);
            String json=new Gson().toJson(censusList);
            return json;

    }

    private void sort(Comparator<IndiaCensusDTO> censusCSVComparator) {
        for(int i=0;i<censusList.size()-1;i++){
            for(int j=0;j<censusList.size()-i-1;j++){
                IndiaCensusDTO census1=censusList.get(j);
                IndiaCensusDTO census2=censusList.get(j+1);
                if(censusCSVComparator.compare(census1,census2)>0){
                    censusList.set(j,census2);
                    censusList.set(j+1,census1);
                }
            }
        }
    }

    public String getPopulationWiseSortCensusData() {
        if(censusMap.size()==0 || censusMap==null){
            throw new CensusAnalyserException(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<IndiaCensusDTO> censusCSVComparator=Comparator.comparing(StateCensus->StateCensus.population);
        this.sort(censusCSVComparator);
        String json=new Gson().toJson(censusList);
        return json;
    }

    public String getDensityWiseSortCensusData() {
        if(censusMap.size()==0 || censusMap==null){
            throw new CensusAnalyserException(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<IndiaCensusDTO> censusCSVComparator=Comparator.comparing(StateCensus->StateCensus.densityPerSqMtr);
        this.sort(censusCSVComparator);
        String json=new Gson().toJson(censusList);
        return json;
    }


}

