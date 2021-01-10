package ru.everybit.bzkpd_bsk.components.building.main.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.everybit.bzkpd_bsk.application.service.attachment.BpmAttachmentService;
import ru.everybit.bzkpd_bsk.model.BpmDaoFactory;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticle;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmArticleType;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmBlock;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmFloor;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmProduct;
import ru.everybit.bzkpd_bsk.model.building.building_object.BpmSector;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.JsonPath;
import com.vaadin.server.FileResource;

public class Check3dModelService{

    private static Pattern NAME_EXTRACT_PATTERN = Pattern.compile("^.*<\\d+ (.+)>$");
//    private static final String MODEL_ARTICLE_FIELD_NAME = "Наименование изделия";
    private static final String MODEL_TYPE_FIELD_NAME = "Type Группа изделия";
    private static final String MODEL_FLOOR_FIELD_NAME = "Уровень";
    private static final String MODEL_BLOCK_FIELD_NAME = "Номер секции";

    public static List<ArticleMergeResult> mergeArticles(Long buildingObjectId, Long modelAttachmentId) {
        // Формируем служебные переменные для мержа
        List<ArticleMergeResult> result = new ArrayList<ArticleMergeResult>();
        HashMap<String, List<ArticleMergeResult>> articleNameMergeResultMap = new HashMap<String, List<ArticleMergeResult>>();

        // Выбирвем все продукты из комплектовочной ведомости
        
            List<BpmProduct> dbProducts = BpmDaoFactory.getProductDao().findByBuildingObjectId(buildingObjectId);
      
        FileResource fileResource = BpmAttachmentService.getAttachmentFileResource(modelAttachmentId);
        Object document = null;
        try{
            document = Configuration.defaultConfiguration().jsonProvider().parse(fileResource.getStream().getStream(), "UTF-8");
        }catch(InvalidJsonException e){
//            BpmAttachmentService.getBpmAttachmentDao().delete(modelAttachmentId);
            return result;
        }
        for (BpmProduct bpmProduct : dbProducts) {
            BpmArticle article = bpmProduct.getBuildingObjectArticle().getArticle();
            BpmArticleType articleType = article.getArticleType();
            BpmSector sector = bpmProduct.getSector();
            BpmBlock block = sector.getBlock();
            BpmFloor floor = sector.getFloor();
            
            List<ArticleMergeResult> mergeResultListByArticle = articleNameMergeResultMap.get(article.getName().trim().toLowerCase());
            if(mergeResultListByArticle == null){
                mergeResultListByArticle = new ArrayList<ArticleMergeResult>();
                articleNameMergeResultMap.put(article.getName().trim().toLowerCase(), mergeResultListByArticle);
            }
            
            for(int i = 0; i < bpmProduct.getQuantity(); i++){
                ArticleMergeResult articleMergeResult = new ArticleMergeResult(null, floor.getName(), null, block.getName(), null, articleType.getName(), null, article.getName());
                mergeResultListByArticle.add(articleMergeResult);
                result.add(articleMergeResult);
            }
        }
        
        List<String> modelName = JsonPath.read(document, "$..object.children[*].name");
        List<Map<String, String>> modelUserDatas = JsonPath.read(document, "$..object.children[*].userData");
        int i = 0;
        for (Map<String, String> userData : modelUserDatas) {
            String nameString = modelName.get(i++);
            Matcher nameMatcher = NAME_EXTRACT_PATTERN.matcher(nameString);
            if(!nameMatcher.find()){
                continue;
            }
            String name = nameMatcher.group(1);
            
            String type = userData.get(MODEL_TYPE_FIELD_NAME);
            String floor = userData.get(MODEL_FLOOR_FIELD_NAME);
            String block = userData.get(MODEL_BLOCK_FIELD_NAME);
            
            if(name == null || floor == null){
                continue;
            }
            List<ArticleMergeResult> articleMergeResults = articleNameMergeResultMap.get(name.trim().toLowerCase());
            if(articleMergeResults == null){
                ArticleMergeResult articleMergeResult = new ArticleMergeResult(floor, null, block, null, type, null, name, null);
                result.add(articleMergeResult);
                continue;
            }
            
            Boolean articleModelIsMerged = false;
            // проход пытаемся связать по этажу, секции, типу и называнию
            for(ArticleMergeResult mergeResult : articleMergeResults){
                // Пропускаем уже смерженные детали из комплектовочной ведомости
                if(mergeResult.getModelName() != null){
                    continue;
                }
                if(!mergeResult.matchFloor(floor)){
                    continue;
                }
                if(!mergeResult.matchBlock(block)){
                    continue;
                }
                if(!mergeResult.matchType(type)){
                    continue;
                }
                mergeResult.setModelName(name);
                mergeResult.setModelFloor(floor);
                mergeResult.setModelType(type);
                mergeResult.setModelBlock(block);
                articleModelIsMerged = true;
                break;
            }
            // проход пытаемся связать по этажу, секции и называнию
            if(!articleModelIsMerged){
                for(ArticleMergeResult mergeResult : articleMergeResults){
                    // Пропускаем уже смерженные детали из комплектовочной ведомости
                    if(mergeResult.getModelName() != null   ){
                        continue;
                    }
                    if(!mergeResult.matchFloor(floor)){
                        continue;
                    }
                    if(!mergeResult.matchBlock(block)){
                        continue;
                    }
                    mergeResult.setModelName(name);
                    mergeResult.setModelFloor(floor);
                    mergeResult.setModelType(type);
                    mergeResult.setModelBlock(block);
                    articleModelIsMerged = true;
                    break;
                }
            }
            
            // Если не нашли заведенную деталь к комплектовочной ведомости, тогда добавляем новый элемент в merge
            if(!articleModelIsMerged){
                ArticleMergeResult articleMergeResult = new ArticleMergeResult(floor, null, block, null, type, null, name, null);
                result.add(articleMergeResult);
                continue;
            }
        }
        
        // Этап проверки на ошибки
        for(ArticleMergeResult articleMergeResult : result){
            if(!articleMergeResult.matchFloor()){
                articleMergeResult.setHasError(true);
                continue;
            }
            if(!articleMergeResult.matchBlock()){
                articleMergeResult.setHasError(true);
                continue;
            }
            if(!articleMergeResult.matchType()){
                articleMergeResult.setHasError(true);
                continue;
            }
            if(!articleMergeResult.matchName()){
                articleMergeResult.setHasError(true);
                continue;
            }
            articleMergeResult.setHasError(false);
        }        
        return result;
    }
    
}
