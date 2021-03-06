package org.javamoney.examples.jaxrs.infrastructure.store;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import org.apache.commons.io.IOUtils;
import org.javamoney.examples.jaxrs.model.Product;

import com.google.gson.Gson;


@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class ProductCollectionReaderProvider implements MessageBodyReader<Collection<Product>> {

	private Gson gson = new Gson();

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		ParameterizedType parametrized = (ParameterizedType)genericType;
		return Collection.class.isAssignableFrom(type) && parametrized.getActualTypeArguments()[0] == Product.class;
	}

	@Override
	public Collection<Product> readFrom(Class<Collection<Product>> type,
			Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {

		String json = IOUtils.toString(entityStream);
		ProductJson[] temps = gson.fromJson(json, ProductJson[].class);
		return Stream.of(temps).map(ProductJson::to).collect(Collectors.toList());
	}

}
