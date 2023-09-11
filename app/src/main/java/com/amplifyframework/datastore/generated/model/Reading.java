package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.core.model.ModelIdentifier;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Reading type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Readings", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class Reading implements Model {
  public static final QueryField ID = field("Reading", "id");
  public static final QueryField USER_ID = field("Reading", "userId");
  public static final QueryField DATE_CREATED = field("Reading", "dateCreated");
  public static final QueryField CARD_ONE_ID = field("Reading", "cardOneId");
  public static final QueryField CARD_TWO_ID = field("Reading", "cardTwoId");
  public static final QueryField CARD_THREE_ID = field("Reading", "cardThreeId");
  public static final QueryField INTERPRETATION = field("Reading", "interpretation");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="Int", isRequired = true) Integer userId;
  private final @ModelField(targetType="AWSDate", isRequired = true) Temporal.Date dateCreated;
  private final @ModelField(targetType="Int", isRequired = true) Integer cardOneId;
  private final @ModelField(targetType="Int", isRequired = true) Integer cardTwoId;
  private final @ModelField(targetType="Int", isRequired = true) Integer cardThreeId;
  private final @ModelField(targetType="String", isRequired = true) String interpretation;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  /** @deprecated This API is internal to Amplify and should not be used. */
  @Deprecated
   public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public Integer getUserId() {
      return userId;
  }
  
  public Temporal.Date getDateCreated() {
      return dateCreated;
  }
  
  public Integer getCardOneId() {
      return cardOneId;
  }
  
  public Integer getCardTwoId() {
      return cardTwoId;
  }
  
  public Integer getCardThreeId() {
      return cardThreeId;
  }
  
  public String getInterpretation() {
      return interpretation;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Reading(String id, Integer userId, Temporal.Date dateCreated, Integer cardOneId, Integer cardTwoId, Integer cardThreeId, String interpretation) {
    this.id = id;
    this.userId = userId;
    this.dateCreated = dateCreated;
    this.cardOneId = cardOneId;
    this.cardTwoId = cardTwoId;
    this.cardThreeId = cardThreeId;
    this.interpretation = interpretation;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Reading reading = (Reading) obj;
      return ObjectsCompat.equals(getId(), reading.getId()) &&
              ObjectsCompat.equals(getUserId(), reading.getUserId()) &&
              ObjectsCompat.equals(getDateCreated(), reading.getDateCreated()) &&
              ObjectsCompat.equals(getCardOneId(), reading.getCardOneId()) &&
              ObjectsCompat.equals(getCardTwoId(), reading.getCardTwoId()) &&
              ObjectsCompat.equals(getCardThreeId(), reading.getCardThreeId()) &&
              ObjectsCompat.equals(getInterpretation(), reading.getInterpretation()) &&
              ObjectsCompat.equals(getCreatedAt(), reading.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), reading.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserId())
      .append(getDateCreated())
      .append(getCardOneId())
      .append(getCardTwoId())
      .append(getCardThreeId())
      .append(getInterpretation())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Reading {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userId=" + String.valueOf(getUserId()) + ", ")
      .append("dateCreated=" + String.valueOf(getDateCreated()) + ", ")
      .append("cardOneId=" + String.valueOf(getCardOneId()) + ", ")
      .append("cardTwoId=" + String.valueOf(getCardTwoId()) + ", ")
      .append("cardThreeId=" + String.valueOf(getCardThreeId()) + ", ")
      .append("interpretation=" + String.valueOf(getInterpretation()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UserIdStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static Reading justId(String id) {
    return new Reading(
      id,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userId,
      dateCreated,
      cardOneId,
      cardTwoId,
      cardThreeId,
      interpretation);
  }
  public interface UserIdStep {
    DateCreatedStep userId(Integer userId);
  }
  

  public interface DateCreatedStep {
    CardOneIdStep dateCreated(Temporal.Date dateCreated);
  }
  

  public interface CardOneIdStep {
    CardTwoIdStep cardOneId(Integer cardOneId);
  }
  

  public interface CardTwoIdStep {
    CardThreeIdStep cardTwoId(Integer cardTwoId);
  }
  

  public interface CardThreeIdStep {
    InterpretationStep cardThreeId(Integer cardThreeId);
  }
  

  public interface InterpretationStep {
    BuildStep interpretation(String interpretation);
  }
  

  public interface BuildStep {
    Reading build();
    BuildStep id(String id);
  }
  

  public static class Builder implements UserIdStep, DateCreatedStep, CardOneIdStep, CardTwoIdStep, CardThreeIdStep, InterpretationStep, BuildStep {
    private String id;
    private Integer userId;
    private Temporal.Date dateCreated;
    private Integer cardOneId;
    private Integer cardTwoId;
    private Integer cardThreeId;
    private String interpretation;
    @Override
     public Reading build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Reading(
          id,
          userId,
          dateCreated,
          cardOneId,
          cardTwoId,
          cardThreeId,
          interpretation);
    }
    
    @Override
     public DateCreatedStep userId(Integer userId) {
        Objects.requireNonNull(userId);
        this.userId = userId;
        return this;
    }
    
    @Override
     public CardOneIdStep dateCreated(Temporal.Date dateCreated) {
        Objects.requireNonNull(dateCreated);
        this.dateCreated = dateCreated;
        return this;
    }
    
    @Override
     public CardTwoIdStep cardOneId(Integer cardOneId) {
        Objects.requireNonNull(cardOneId);
        this.cardOneId = cardOneId;
        return this;
    }
    
    @Override
     public CardThreeIdStep cardTwoId(Integer cardTwoId) {
        Objects.requireNonNull(cardTwoId);
        this.cardTwoId = cardTwoId;
        return this;
    }
    
    @Override
     public InterpretationStep cardThreeId(Integer cardThreeId) {
        Objects.requireNonNull(cardThreeId);
        this.cardThreeId = cardThreeId;
        return this;
    }
    
    @Override
     public BuildStep interpretation(String interpretation) {
        Objects.requireNonNull(interpretation);
        this.interpretation = interpretation;
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, Integer userId, Temporal.Date dateCreated, Integer cardOneId, Integer cardTwoId, Integer cardThreeId, String interpretation) {
      super.id(id);
      super.userId(userId)
        .dateCreated(dateCreated)
        .cardOneId(cardOneId)
        .cardTwoId(cardTwoId)
        .cardThreeId(cardThreeId)
        .interpretation(interpretation);
    }
    
    @Override
     public CopyOfBuilder userId(Integer userId) {
      return (CopyOfBuilder) super.userId(userId);
    }
    
    @Override
     public CopyOfBuilder dateCreated(Temporal.Date dateCreated) {
      return (CopyOfBuilder) super.dateCreated(dateCreated);
    }
    
    @Override
     public CopyOfBuilder cardOneId(Integer cardOneId) {
      return (CopyOfBuilder) super.cardOneId(cardOneId);
    }
    
    @Override
     public CopyOfBuilder cardTwoId(Integer cardTwoId) {
      return (CopyOfBuilder) super.cardTwoId(cardTwoId);
    }
    
    @Override
     public CopyOfBuilder cardThreeId(Integer cardThreeId) {
      return (CopyOfBuilder) super.cardThreeId(cardThreeId);
    }
    
    @Override
     public CopyOfBuilder interpretation(String interpretation) {
      return (CopyOfBuilder) super.interpretation(interpretation);
    }
  }
  

  public static class ReadingIdentifier extends ModelIdentifier<Reading> {
    private static final long serialVersionUID = 1L;
    public ReadingIdentifier(String id) {
      super(id);
    }
  }
  
}
