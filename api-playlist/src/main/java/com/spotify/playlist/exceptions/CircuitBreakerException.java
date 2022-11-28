package com.spotify.playlist.exceptions;

public class CircuitBreakerException extends Exception{

    public CircuitBreakerException(String msg){
        super(msg);
    }
}
