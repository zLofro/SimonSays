package vermillion.productions.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;

/**
 * Recipe Builder
 * <p>Clase creada para crear Recipes más fácil</p>
 * @author Agus5534
 * @version 1.0
 */
public class RecipeBuilder {

    private final Type type;
    private final ItemStack result;
    private final String name;
    private String[] pattern;
    private String group;
    private final Recipe recipe;

    /**
     * Constructor de la clase.
     * @param type Tipo de recipe.
     * @param result Resultado de la recipe.
     * @param name Nombre de la recipe.
     * @throws NullPointerException Cuando {@link Type} es nulo.
     */
    public RecipeBuilder(Type type, @NotNull ItemStack result, @NotNull String name) {
        this.type = type;
        this.result = result;
        this.name = name;

        switch (type) {
            case SHAPED -> recipe = new ShapedRecipe(new NamespacedKey(NamespacedKey.MINECRAFT, name), result);
            case SHAPELESS -> recipe = new ShapelessRecipe(new NamespacedKey(NamespacedKey.MINECRAFT, name), result);
            default -> throw new NullPointerException("Missing Recipe Type");
        }
    }

    /**
     * Constructor de la clase
     * @param type Tipo de recipe.
     * @param result Resultado de la recipe.
     * @param name Nombre de la recipe.
     * @param namespace Identificador de la recipe.
     * @throws NullPointerException Cuando {@link Type} es nulo.
     */
    public RecipeBuilder(Type type, @NotNull ItemStack result, @NotNull String name, @NotNull String namespace) {
        this.type = type;
        this.result = result;
        this.name = name;

        switch (type) {
            case SHAPED -> recipe = new ShapedRecipe(new NamespacedKey(namespace, name), result);
            case SHAPELESS -> recipe = new ShapelessRecipe(new NamespacedKey(namespace, name), result);
            default -> throw new NullPointerException("Missing Recipe Type");
        }
    }

    /**
     * Establece el patrón de la receta.
     * @param pattern Patrón a utilizar para el crafteo de la receta.
     * @return {@link RecipeBuilder}
     * @see ShapedRecipe
     * @throws IllegalArgumentException Cuando {@link Type} es {@link Type#SHAPELESS}
     */
    public RecipeBuilder setPattern(String... pattern) {
        if(type == Type.SHAPELESS) {
            throw new IllegalArgumentException("Cannot set pattern of a Shapeless recipe.");
        }

        this.pattern = pattern;

        return this;
    }

    /**
     * Agrega ingredients a la receta.
     * @param materials Materiales a agregar.
     * @return {@link RecipeBuilder}
     * @see ShapelessRecipe
     * @throws IllegalArgumentException Cuando {@link Type} es {@link Type#SHAPED}
     */
    public RecipeBuilder addIngredients(Material... materials) {
        if(type == Type.SHAPED) {
            throw new IllegalArgumentException("Shaped recipes requires Characters with ItemStacks.");
        }

        Arrays.stream(materials).toList().forEach(m -> ((ShapelessRecipe)recipe).addIngredient(m));

        return this;
    }
    /**
     * Agrega ingredients a la receta.
     * @param itemStacks ItemStacks para agregar.
     * @return {@link RecipeBuilder}
     * @see ShapelessRecipe
     * @throws IllegalArgumentException Cuando {@link Type} es {@link Type#SHAPED}
     */
    public RecipeBuilder addIngredients(ItemStack... itemStacks) {
        if(type == Type.SHAPED) {
            throw new IllegalArgumentException("Shaped recipes requires Characters with ItemStacks.");
        }

        Arrays.stream(itemStacks).toList().forEach(i -> ((ShapelessRecipe)recipe).addIngredient(i));

        return this;
    }

    /**
     * Establece los ingredientes de la receta.
     * @param ingredients Mapa con los ingredientes.
     * @return {@link RecipeBuilder}
     * @see ShapedRecipe
     * @throws IllegalArgumentException Cuando {@link Type} es {@link Type#SHAPELESS}
     */
    public RecipeBuilder setIngredients(Map<Character, ItemStack> ingredients) {
        if(type == Type.SHAPELESS) {
            throw new IllegalArgumentException("Shapeless recipes only requires ingredients.");
        }

        ingredients.keySet().forEach(c -> ((ShapedRecipe)recipe).setIngredient(c, ingredients.get(c)));

        return this;
    }

    /**
     * Establece el grupo de la receta.
     * @param group Grupo de la receta.
     * @return {@link RecipeBuilder}
     * @throws NullPointerException Cuando {@link Type} es nulo.
     */
    public RecipeBuilder setGroup(String group) {
        this.group = group;

        switch (type) {
            case SHAPED ->  ((ShapedRecipe)recipe).setGroup(group);
            case SHAPELESS ->  ((ShapelessRecipe)recipe).setGroup(group);
            default -> throw new NullPointerException("Missing Recipe Type");
        }

        return this;
    }

    /**
     * Crea y agrega la receta al juego.
     * @return {@link Recipe}
     * @throws NullPointerException Si pattern es nulo.
     */
    public Recipe build() {
        if(type == Type.SHAPED) {
            if(pattern == null) {
                throw new NullPointerException("pattern is null");
            }

            ((ShapedRecipe)recipe).shape(pattern);
        }

        Bukkit.addRecipe(recipe);

        return recipe;
    }


    /**
     * Es el tipo de receta.
     */
    public enum Type {
        SHAPED,
        SHAPELESS
    }

    public Type getType() {
        return type;
    }

    public ItemStack getResult() {
        return result;
    }

    public String getName() {
        return name;
    }

    public String[] getPattern() {
        return pattern;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public String getGroup() {
        return group;
    }
}