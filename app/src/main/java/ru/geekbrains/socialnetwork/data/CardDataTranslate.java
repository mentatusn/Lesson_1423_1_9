package ru.geekbrains.socialnetwork.data;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CardDataTranslate {
    public  static class Fields{
        public final static String PICTURE = "picture";
        public final static String DATE = "date";
        public final static String TITLE = "title";
        public final static String DESCRIPTION = "description";
        public final static String LIKE = "like";
    }

    public static CardData documentToCardData(String id, Map<String,Object> doc){
        CardData answer = new CardData(
                (String) doc.get(Fields.TITLE),
                (String) doc.get(Fields.DESCRIPTION),
                PictureIndexConverter.getPictureByIndex( Math.toIntExact((Long) doc.get(Fields.PICTURE))), // TODO хотим побаловаться с int
                (boolean) doc.get(Fields.LIKE),
                ((Timestamp) doc.get(Fields.DATE)).toDate()); // TODO узнать про картинки
        answer.setId(id);
        return answer;
    }

    public static Map<String,Object> cardDataToDocument( CardData cardData){
        Map<String,Object> answer = new HashMap<>();
        answer.put(Fields.TITLE,cardData.getTitle());
        answer.put(Fields.DESCRIPTION,cardData.getDescription());
        answer.put(Fields.PICTURE,PictureIndexConverter.getIndexByPicture(cardData.getPicture()));
        answer.put(Fields.LIKE,cardData.isLike());
        answer.put(Fields.DATE,cardData.getDate());
        return answer;
    }

}
