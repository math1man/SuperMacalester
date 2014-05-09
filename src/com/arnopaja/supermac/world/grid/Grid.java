package com.arnopaja.supermac.world.grid;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.world.objects.Entity;
import com.arnopaja.supermac.world.objects.Tile;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

/**
 * Superclass for all types of grids.
 *
 * @author Ari Weiland
 */
public class Grid {

    // the pixel width and height of a grid space
    public static final int GRID_PIXEL_DIMENSION = 32;
    public static final int RENDER_GRID_WIDTH = getRenderDimension(GameScreen.GAME_WIDTH);
    public static final int RENDER_GRID_HEIGHT = getRenderDimension(GameScreen.GAME_HEIGHT);
    public static final Vector2 RENDER_GRID_OFFSET = new Vector2(GameScreen.GAME_WIDTH, GameScreen.GAME_HEIGHT)
            .scl(1f / Grid.GRID_PIXEL_DIMENSION)
            .sub(new Vector2(Grid.RENDER_GRID_WIDTH, Grid.RENDER_GRID_HEIGHT))
            .scl(0.5f);

    protected final int gridWidth, gridHeight;
    private final String name;

    protected Tile[][] tileMatrix;
    protected Map<Vector2, Entity> entityMap;

    public Grid(String name, int gridWidth, int gridHeight) {
        this(name, new Tile[gridWidth][gridHeight]);
    }

    public Grid(String name, Tile[][] tileMatrix) {
        this(name, tileMatrix, new Hashtable<Vector2, Entity>());
    }

    public Grid(String name, Tile[][] tileMatrix, Map<Vector2, Entity> entityMap) {
        this.name = name;
        this.tileMatrix = tileMatrix;
        this.entityMap = entityMap;
        gridWidth = tileMatrix.length;
        gridHeight = tileMatrix[0].length;
    }

    public Grid(Grid grid) {
        this(grid.getName(), grid.tileMatrix, grid.entityMap);
    }

    /**
     * Returns the entity at the specified position,
     * or null if no entity exists there.
     *
     * @param position the position of the entity
     * @return the entity at the specified coordinates, or null
     */
    public Entity getEntity(Vector2 position) {
        return entityMap.get(position);
    }

    /**
     * Puts the entity in the map at its specified position.
     * Returns the entity previously at that location, or null if none existed.
     *
     * @param entity the entity to be placed in the map
     * @return the entity previously at that location
     */
    public Entity putEntity(Entity entity) {
        Vector2 position = entity.getPosition();
        if (!isInBounds(position)) {
            throw new IndexOutOfBoundsException();
        }
        return entityMap.put(position, entity);
    }

    /**
     * Moves the specified entity in the specified direction.
     *
     * @param entity the entity to be moved
     * @param dir the direction in which to move
     * @return true if an entity was moved, else false
     */
    public boolean moveEntity(Entity entity, Direction dir) {
        if (entity != null) {
            Vector2 position = entity.getPosition();
            Vector2 destination = Direction.getAdjacent(position, dir);
            if (isPathable(destination)) {
                removeEntity(position);
                entity.setPosition(destination);
                putEntity(entity);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the entity at the specified position.
     * Returns the entity removed, or null if there was no entity there.
     *
     * @param position the coordinates of the position
     * @return the entity removed, or null if there was no entity there
     */
    public Entity removeEntity(Vector2 position) {
        return entityMap.remove(position);
    }

    public Collection<Entity> getEntities() {
        return entityMap.values();
    }

    public void clear() {
        entityMap.clear();
    }

    /**
     * Returns a RenderGrid of the calling grid centered at the given position.
     * If the RenderGrid covers spaces not in the calling grid, those spaces will be
     * set as blank elements.
     *
     * @param position the coordinates of the center of the RenderGrid
     * @return the RenderGrid
     */
    public RenderGrid getRenderGrid(Vector2 position) {
        Vector2 corner = new Vector2(RENDER_GRID_WIDTH, RENDER_GRID_HEIGHT)
                .add(-1, -1).scl(-0.5f).add(position);
        return new RenderGrid(getSubGrid(corner, RENDER_GRID_WIDTH, RENDER_GRID_HEIGHT));
    }

    /**
     * Returns a grid that represents a portion of the current grid, with the upper
     * left hand corner specified by corner. Space outside of the initial grid will
     * be filled in by null tiles.
     *
     * @param corner the upper left hand corner of the subgrid
     * @param width the width of the subgrid
     * @param height the height of the subgrid
     * @return
     */
    public Grid getSubGrid(Vector2 corner, int width, int height) {
        return new Grid(getName() + " " + corner + " " + width + " " + height,
                getSubTileGrid(corner, width, height), getSubEntityMap(corner, width, height));
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public Tile getTile(Vector2 position) {
        return tileMatrix[cast(position.x)][cast(position.y)];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Grid)) return false;

        Grid grid = (Grid) o;

        return Arrays.equals(this.tileMatrix, grid.tileMatrix);
    }

    protected Tile[][] getSubTileGrid(Vector2 corner, int width, int height) {
        Tile[][] subTileArray = new Tile[width][height];
        for (int i=0; i<width; i++) {
            for (int j=0; j<height; j++) {
                Vector2 position = new Vector2(i, j).add(corner);
                if (isInBounds(position)) {
                    subTileArray[i][j] = tileMatrix[cast(position.x)][cast(position.y)];
                } else {
                    subTileArray[i][j] = Tile.NULL;
                }
            }
        }
        return subTileArray;
    }

    protected Map<Vector2, Entity> getSubEntityMap(Vector2 corner, int width, int height) {
        Map<Vector2, Entity> subEntityMap = new Hashtable<Vector2, Entity>();
        for (int i=0; i<width; i++) {
            for (int j=0; j<height; j++) {
                Vector2 newKey = new Vector2(i, j);
                Vector2 key = corner.cpy().add(newKey);
                if (entityMap.containsKey(key)) {
                    subEntityMap.put(newKey, getEntity(key));
                }
            }
        }
        return subEntityMap;
    }

    public boolean isInBounds(int x, int y) {
        return ((x < gridWidth)
                && (x >= 0)
                && (y < gridHeight)
                && (y >= 0));
    }

    public boolean isInBounds(Vector2 position) {
        return isInBounds(cast(position.x), cast(position.y));
    }

    public boolean isPathable(Vector2 position) {
        return (isInBounds(position)
                && (getEntity(position) == null)
                && getTile(position).isPathable());
    }

    protected static int cast(float f) {
        return (int) f;
    }

    protected static int getRenderDimension(float gameDimension) {
        int renderDimension = (int) Math.ceil(gameDimension / Grid.GRID_PIXEL_DIMENSION);
        if (renderDimension % 2 == 0) {
            renderDimension += 3;
        } else {
            renderDimension += 2;
        }
        return renderDimension;
    }

    public String getName() {
        return name;
    }
}
