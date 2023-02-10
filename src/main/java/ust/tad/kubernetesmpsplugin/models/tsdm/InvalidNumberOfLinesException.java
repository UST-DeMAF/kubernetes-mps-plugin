package ust.tad.kubernetesmpsplugin.models.tsdm;

public class InvalidNumberOfLinesException extends Exception{
    public InvalidNumberOfLinesException(String errorMessage) {
        super(errorMessage);
    }  
}
