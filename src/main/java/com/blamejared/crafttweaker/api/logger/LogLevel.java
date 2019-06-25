package com.blamejared.crafttweaker.api.logger;

public enum LogLevel {
    
    DEBUG, INFO, WARNING, ERROR;
    
    public boolean canLog(LogLevel other) {
        return this.compareTo(other) <= 0;
    }
    
}
