package com.example.springbootexcel.we;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)

public class OptionReq implements Serializable
{
   private static final long serialVersionUID = 1L;

   @Valid
   @JsonProperty("optionId")
   @JsonSerialize(using = ToStringSerializer.class)
   private Long optionId = null;

   @Valid
   @JsonProperty("optionContent")
   @Length(max = 500)
   private String optionContent = null;

   @Valid
   @JsonProperty("optionOrder")
   private Integer optionOrder = null;

   @Valid
   @JsonProperty("isOther")
   private Boolean isOther = null;

   @Valid
   @JsonProperty("properties")
   private List<PropertyVO> properties = new ArrayList<PropertyVO>();

   public OptionReq()
   {
       super();
   }

   /**
    * 选项id
   **/
   public Long getOptionId()
   {
       return optionId;
   }
   public void setOptionId(Long optionId_in)
   {
       this.optionId = optionId_in;
   }

   /**
    * 选项内容
   **/
   public String getOptionContent()
   {
       return optionContent;
   }
   public void setOptionContent(String optionContent_in)
   {
       this.optionContent = optionContent_in;
   }

   /**
    * 选项序号
   **/
   public Integer getOptionOrder()
   {
       return optionOrder;
   }
   public void setOptionOrder(Integer optionOrder_in)
   {
       this.optionOrder = optionOrder_in;
   }

   /**
    * 是否为其他选项
   **/
   public Boolean getIsOther()
   {
       return isOther;
   }
   public void setIsOther(Boolean isOther_in)
   {
       this.isOther = isOther_in;
   }

   /**
   **/
   public List<PropertyVO> getProperties()
   {
       return properties;
   }
   public void setProperties(List<PropertyVO> properties_in)
   {
       this.properties = properties_in;
   }










}
