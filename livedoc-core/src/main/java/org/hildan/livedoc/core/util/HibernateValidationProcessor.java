package org.hildan.livedoc.core.util;

import java.lang.reflect.AnnotatedElement;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;
import org.hildan.livedoc.core.pojo.ApiObjectFieldDoc;

public class HibernateValidationProcessor {

    private static final String AssertFalse_message = "must be false";

    private static final String AssertTrue_message = "must be true";

    private static final String DecimalMax_message = "must be less than %s %s";

    private static final String DecimalMin_message = "must be greater than %s %s";

    private static final String Digits_message = "numeric value made of <%s digits>.<%s digits>)";

    private static final String Future_message = "must be in the future";

    private static final String Max_message = "must be less than or equal to %s";

    private static final String Min_message = "must be greater than or equal to %s";

    private static final String NotNull_message = "may not be null";

    private static final String Null_message = "must be null";

    private static final String Past_message = "must be in the past";

    private static final String Pattern_message = "must match %s";

    private static final String Size_message = "size must be between %s and %s";

    private static final String Email_message = "must be a well-formed email address";

    private static final String Length_message = "length must be between %s and %s";

    private static final String NotBlank_message = "may not be empty";

    private static final String NotEmpty_message = "may not be empty";

    private static final String Range_message = "must be between %s and %s";

    private static final String URL_message = "must be a valid URL";

    private static final String CreditCardNumber_message = "must be a valid credit card number";

    private static final String ScriptAssert_message = "script expression %s didn't evaluate to true";

    public static void addConstraintMessages(AnnotatedElement property, ApiObjectFieldDoc apiPojoFieldDoc) {
        try {
            Class.forName("org.hibernate.validator.constraints.NotBlank");

            if (property.isAnnotationPresent(AssertFalse.class)) {
                apiPojoFieldDoc.addFormat(AssertFalse_message);
            }

            if (property.isAnnotationPresent(AssertTrue.class)) {
                apiPojoFieldDoc.addFormat(AssertTrue_message);
            }

            if (property.isAnnotationPresent(NotNull.class)) {
                apiPojoFieldDoc.addFormat(NotNull_message);
            }

            if (property.isAnnotationPresent(Null.class)) {
                apiPojoFieldDoc.addFormat(Null_message);
            }

            if (property.isAnnotationPresent(Size.class)) {
                Size annotation = property.getAnnotation(Size.class);
                apiPojoFieldDoc.addFormat(String.format(Size_message, annotation.min(), annotation.max()));
            }

            if (property.isAnnotationPresent(NotBlank.class)) {
                apiPojoFieldDoc.addFormat(NotBlank_message);
            }

            if (property.isAnnotationPresent(NotEmpty.class)) {
                apiPojoFieldDoc.addFormat(NotEmpty_message);
            }

            if (property.isAnnotationPresent(Length.class)) {
                Length annotation = property.getAnnotation(Length.class);
                apiPojoFieldDoc.addFormat(String.format(Length_message, annotation.min(), annotation.max()));
            }

            if (property.isAnnotationPresent(Range.class)) {
                Range annotation = property.getAnnotation(Range.class);
                apiPojoFieldDoc.addFormat(String.format(Range_message, annotation.min(), annotation.max()));
            }

            if (property.isAnnotationPresent(DecimalMax.class)) {
                DecimalMax annotation = property.getAnnotation(DecimalMax.class);
                apiPojoFieldDoc.addFormat(String.format(DecimalMax_message, annotation.inclusive() ? "or equal to" : "",
                        annotation.value()));
            }

            if (property.isAnnotationPresent(DecimalMin.class)) {
                DecimalMin annotation = property.getAnnotation(DecimalMin.class);
                apiPojoFieldDoc.addFormat(String.format(DecimalMin_message, annotation.inclusive() ? "or equal to" : "",
                        annotation.value()));
            }

            if (property.isAnnotationPresent(Future.class)) {
                apiPojoFieldDoc.addFormat(Future_message);
            }

            if (property.isAnnotationPresent(Past.class)) {
                apiPojoFieldDoc.addFormat(Past_message);
            }

            if (property.isAnnotationPresent(Digits.class)) {
                Digits annotation = property.getAnnotation(Digits.class);
                apiPojoFieldDoc.addFormat(String.format(Digits_message, annotation.integer(), annotation.fraction()));
            }

            if (property.isAnnotationPresent(Max.class)) {
                Max annotation = property.getAnnotation(Max.class);
                apiPojoFieldDoc.addFormat(String.format(Max_message, annotation.value()));
            }

            if (property.isAnnotationPresent(Min.class)) {
                Min annotation = property.getAnnotation(Min.class);
                apiPojoFieldDoc.addFormat(String.format(Min_message, annotation.value()));
            }

            if (property.isAnnotationPresent(Pattern.class)) {
                Pattern annotation = property.getAnnotation(Pattern.class);
                apiPojoFieldDoc.addFormat(String.format(Pattern_message, annotation.regexp()));
            }

            if (property.isAnnotationPresent(Email.class)) {
                apiPojoFieldDoc.addFormat(Email_message);
            }

            if (property.isAnnotationPresent(URL.class)) {
                apiPojoFieldDoc.addFormat(URL_message);
            }

            if (property.isAnnotationPresent(CreditCardNumber.class)) {
                apiPojoFieldDoc.addFormat(CreditCardNumber_message);
            }

        } catch (ClassNotFoundException e) {
            // nothing to do here
        }

    }

}
