package tacos.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import tacos.Ingredient;
import tacos.Ingredient.Type;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JdbcIngredientRepository implements IngredientRepository {

    private static final String QUERY_FIND_ALL = "SELECT id, name, type FROM Ingredient";
    private static final String QUERY_FIND_ONE = "SELECT id, name, type FROM Ingredient WHERE id=?";
    public static final String QUERY_SAVE = "INSERT INTO Ingredient (id, name, type) VALUES (?, ?, ?)";

    private JdbcTemplate jdbc;

    @Autowired
    public JdbcIngredientRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Iterable<Ingredient> findAll() {
        return jdbc.query(QUERY_FIND_ALL, this::mapRowToIngredient);
    }

    @Override
    public Ingredient findOne(String id) {
        return jdbc.queryForObject(QUERY_FIND_ONE, this::mapRowToIngredient, id);
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        jdbc.update(QUERY_SAVE, ingredient.getId(), ingredient.getName(), ingredient.getType().toString());
        return ingredient;
    }

    private Ingredient mapRowToIngredient(ResultSet rs, int rowNum) throws SQLException {
        String id = rs.getString("id");
        String name = rs.getString("name");
        Type type = Type.valueOf(rs.getString("type"));
        return new Ingredient(id, name, type);
    }
}
