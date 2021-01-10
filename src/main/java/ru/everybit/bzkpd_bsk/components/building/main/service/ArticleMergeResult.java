package ru.everybit.bzkpd_bsk.components.building.main.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArticleMergeResult {
    
    private static final Pattern FLOOR_PATTERN = Pattern.compile("\\D*([0-9]+)\\D*$");
    String modelFloor;
    String dbFloor;

    String modelBlock;
    String dbBlock;

    String modelType;
    String dbType;

    String modelName;
    String dbName;
    
    private Boolean hasError = true;

    public ArticleMergeResult(String modelFloor, String dbFloor,
            String modelBlock, String dbBlock, String modelType, String dbType,
            String modelName, String dbName) {

        this.modelFloor = modelFloor;
        this.dbFloor = dbFloor;
        this.modelBlock = modelBlock;
        this.dbBlock = dbBlock;
        this.modelType = modelType;
        this.dbType = dbType;
        this.modelName = modelName;
        this.dbName = dbName;

    }

    public Boolean matchFloor(String modelFloor){
        if(dbFloor == null || modelFloor == null)
            return false;
        
        Matcher dbFloorMatcher = FLOOR_PATTERN.matcher(dbFloor.trim());
        Boolean isDbCherdak = !dbFloorMatcher.find();
        
        Matcher modelFloorMatcher = FLOOR_PATTERN.matcher(modelFloor.trim());
        Boolean isModelCherdak = !modelFloorMatcher.find();
        
        // Если и в ведомости и в модели чердак
        if(isDbCherdak && isModelCherdak)
            return true;
        
        // Если один чердак, а второй нет
        if(!isDbCherdak.equals(isModelCherdak)){
            return false;
        }
        String dbFloorNumber = dbFloorMatcher.group(1);
        String modelFloorNumber = modelFloorMatcher.group(1);
        
        return dbFloorNumber.equals(modelFloorNumber);
    }
    
    public Boolean matchFloor(){
        return matchFloor(this.modelFloor);
    }
    
    public Boolean matchBlock(String modelBlock){
        return dbBlock != null && modelBlock != null && dbBlock.trim().equalsIgnoreCase(modelBlock.trim());
    }
    
    public Boolean matchBlock(){
        return matchBlock(modelBlock);
    }

    public Boolean matchType(){
        return matchType(modelType);
    }
    
    public Boolean matchType(String modelType){
        return dbType != null && modelType != null && dbType.trim().equalsIgnoreCase(modelType.trim());
    }
    
    public Boolean matchName(){
        return dbName != null && modelName != null && dbName.trim().equalsIgnoreCase(modelName.trim());
    }
    
    public String getModelFloor() {
        return modelFloor;
    }

    public void setModelFloor(String modelFloor) {
        this.modelFloor = modelFloor;
    }

    public String getDbFloor() {
        return dbFloor;
    }

    public void setDbFloor(String dbFloor) {
        this.dbFloor = dbFloor;
    }

    public String getModelBlock() {
        return modelBlock;
    }

    public void setModelBlock(String modelBlock) {
        this.modelBlock = modelBlock;
    }

    public String getDbBlock() {
        return dbBlock;
    }

    public void setDbBlock(String dbBlock) {
        this.dbBlock = dbBlock;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public Boolean getHasError() {
        return hasError;
    }

    public void setHasError(Boolean hasError) {
        this.hasError = hasError;
    }
}
