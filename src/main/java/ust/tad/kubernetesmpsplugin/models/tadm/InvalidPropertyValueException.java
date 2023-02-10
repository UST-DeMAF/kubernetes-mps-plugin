package ust.tad.kubernetesmpsplugin.models.tadm;

public class InvalidPropertyValueException extends Exception{
    public InvalidPropertyValueException(String errorMessage) {
        super(errorMessage);
    }    
}
