package models;


import play.data.validation.Constraints;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

import play.db.ebean.Model;
import play.libs.F;
import play.mvc.PathBindable;
import play.libs.F.Option;
import play.mvc.QueryStringBindable;

public class Product implements PathBindable<Product>,
        QueryStringBindable<Product> {

    public static class EanValidator extends Constraints.Validator<String> {
        @Override
        public boolean isValid(String value) {
            String pattern = "^[0-9]{13}$";
            return value != null && value.matches(pattern);
        }
        @Override
        public F.Tuple<String, Object[]> getErrorMessageKey() {
            return new F.Tuple<String, Object[]>("error.invalid.ean",new Object[]{});
        }
    }

    private static List<Product> products;

    static {
        products = new ArrayList<Product>();
        products.add(new Product("1111111111111", "Paperclips 1",
                "Paperclips description 1"));
        products.add(new Product("2222222222222", "Paperclips 2",
                "Paperclips description 2"));
        products.add(new Product("3333333333333", "Paperclips 3",
                "Paperclips description 3"));
        products.add(new Product("4444444444444", "Paperclips 4",
                "Paperclips description 4"));
        products.add(new Product("5555555555555", "Paperclips 5",
                "Paperclips description 5"));
    }



    @Constraints.Required
    public String ean;
    @Constraints.Required
    public String name;
    public String description;

    public byte[] picture;

    public List<Tag> tags = new LinkedList<Tag>();

    public Product() {
    }

    public Product(String ean, String name, String description) {
        this.ean = ean;
        this.name = name;
        this.description = description;
    }

    public String toString() {
        return String.format("%s - %s", ean, name);
    }

    public static List<Product> findAll() {
        return new ArrayList<Product>(products);
    }

    public static Product findByEan(String ean) {
        for (Product candidate : products) {
            if (candidate.ean.equals(ean)) {
                return candidate;
            }
        }
        return null;
    }

    public static List<Product> findByName(String term) {
        final List<Product> results = new ArrayList<Product>();
        for (Product candidate : products) {
            if (candidate.name.toLowerCase().contains(term.toLowerCase())) {
                results.add(candidate);
            }
        }

        return results;
    }

    public static boolean remove(Product product) {
        return products.remove(product);
    }

    public void save() {
        products.remove(findByEan(this.ean));
        products.add(this);
    }

    @Override
    public Product bind (String key, String value) {
        return findByEan(value);
    }
    @Override
    public Option<Product> bind(String key, Map<String, String[]> data) {
        return Option.Some(findByEan(data.get("ean")[0]));
    }
    @Override
    public String unbind(String key) {
        return this.ean;
    }
    @Override
    public String javascriptUnbind() {
        return this.ean;
    }
}

