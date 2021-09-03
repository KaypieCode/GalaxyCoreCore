package net.galaxycore.galaxycorecore.permissions.exceptions;

public class MissingFieldException extends RuntimeException{
    public MissingFieldException(String s) {
        super("Missing Field: " + s);
    }
}
