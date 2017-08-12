package org.hildan.livedoc.core.scanner.builder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hildan.livedoc.core.annotation.ApiErrors;
import org.hildan.livedoc.core.pojo.ApiErrorDoc;
import org.hildan.livedoc.core.annotation.ApiError;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

public class JSONDocApiErrorDocBuilder {
	
	public static List<ApiErrorDoc> build(Method method) {
		List<ApiErrorDoc> apiMethodDocs = new ArrayList<ApiErrorDoc>();

		ApiErrors methodAnnotation = method.getAnnotation(ApiErrors.class);
		ApiErrors typeAnnotation = method.getDeclaringClass().getAnnotation(ApiErrors.class);

		if(methodAnnotation != null) {
			for (ApiError apiError : methodAnnotation.apierrors()) {
				apiMethodDocs.add(new ApiErrorDoc(apiError.code(), apiError.description()));
			}
		}

		if(typeAnnotation != null) {
			for (final ApiError apiError : typeAnnotation.apierrors()) {

				boolean isAlreadyDefined = FluentIterable.from(apiMethodDocs).anyMatch(new Predicate<ApiErrorDoc>() {
					@Override
					public boolean apply(ApiErrorDoc apiErrorDoc) {
						return apiError.code().equals(apiErrorDoc.getCode());
					};
				});

				if (!isAlreadyDefined) {
					apiMethodDocs.add(new ApiErrorDoc(apiError.code(), apiError.description()));
				}
			}
		}
		return apiMethodDocs;
	}

}
