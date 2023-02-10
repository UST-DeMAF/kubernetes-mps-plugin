package ust.tad.kubernetesmpsplugin.models.tsdm;

public class InvalidAnnotationException extends Exception {
    public InvalidAnnotationException(String errorMessage) {
        super(errorMessage);
    }    
}
