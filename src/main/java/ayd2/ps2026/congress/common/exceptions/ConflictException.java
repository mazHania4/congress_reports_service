package ayd2.ps2026.congress.common.exceptions;

public class ConflictException extends Exception{
    public ConflictException( String message) {
        super(message);
    }
    public ConflictException(){
        super();
    }
}
