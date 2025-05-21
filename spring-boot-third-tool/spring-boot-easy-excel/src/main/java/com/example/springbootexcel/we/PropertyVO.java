package com.example.springbootexcel.we;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown = true)

public class PropertyVO implements Serializable
{
   private static final long serialVersionUID = 1L;

   @Valid
   @JsonProperty("propertyKey")
   private String propertyKey = null;

   @Valid
   @JsonProperty("propertyValue")
   private String propertyValue = null;

   public PropertyVO()
   {
       super();
   }

   /**
    * 参数key
   **/
   public String getPropertyKey()
   {
       return propertyKey;
   }
   public void setPropertyKey(String propertyKey_in)
   {
       this.propertyKey = propertyKey_in;
   }

   /**
    * 参数值
   **/
   public String getPropertyValue()
   {
       return propertyValue;
   }
   public void setPropertyValue(String propertyValue_in)
   {
       this.propertyValue = propertyValue_in;
   }










}
