package com.arnopaja.supermac.world.grid;

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

    private final String name;
    protected final int gridWidth, gridHeight;

    protected Tile[][] tileArray;
    protected Map<Vector2, Entity> entityMap;

    public Grid(String name, int gridWidth, int gridHeight) {
        this(name, new Tile[gridWidth][gridHeight]);
    }

    public Grid(String name, Tile[][] tileArray) {
        this(name, tileArray, new Hashtable<Vector2, Entity>());
    }

    public Grid(String gridName, Tile[][] tileArray, Map<Vector2, Entity> entityMap) {
        this.name = gridName;
        this.tileArray = tileArray;
        this.entityMap = entityMap;
        gridWidth = tileArray.length;
        gridHeight = tileArray[0].length;
    }

    public Grid(Grid grid) {
        this(grid.getName(), grid.getTileGrid(), grid.getEntityMap());
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

    public Location getNearestValidLocation(Location location) {
        return getNearestValidLocation(location.getPosition(), location.getFacing());
    }

    /**
     * Returns the nearest valid location to the specified position.
     * If the position is off the grid, it tests the nearest position in the grid.
     * If the position in the grid is blocked, it tests the next position
     * @param position
     * @param direction
     * @return
     */
    public Location getNearestValidLocation(Vector2 position, Direction direction) {
        if (isPathable(position)) {
            // The current position is valid and pathable
            return new Location(this, position, direction);
        } else if (position.x < 0) {
            // These next four cases handle the position being invalid
            return getNearestValidLocation(new Vector2(0, position.y), direction);
        } else if (position.x >= gridWidth) {
            return getNearestValidLocation(new Vector2(gridWidth - 1, position.y), direction);
        } else if (position.y < 0) {
            return getNearestValidLocation(new Vector2(position.x, 0), direction);
        } else if (position.y >= gridHeight) {
            return getNearestValidLocation(new Vector2(position.x, gridHeight - 1), direction);
        } else {
            // The current position is valid but not pathable
            Vector2 newPosition = Direction.getAdjacent(position, direction);
            if (!isInBounds(newPosition)) {
                // This is the case that it has found no pathable position in the direction
                return null; // TODO: is there a better way to handle this case?
            }
            return getNearestValidLocation(newPosition, direction);
        }
    }

    /**
     * Returns a RenderGrid of the calling grid centered at the given position.
     * If the RenderGrid covers spaces not in the calling grid, those spaces will be
     * set as blank elements.
     *
     * @param position the coordinates of the center of the RenderGrid
     * @param renderGridWidth the width of the RenderGrid
     * @param renderGridHeight the height of the RenderGrid
     * @return the RenderGrid
     */
    public RenderGrid getRenderGrid(Vector2 position, int renderGridWidth, int renderGridHeight) {
        Vector2 corner = new Vector2(renderGridWidth, renderGridHeight)
                .add(-1, -1).scl(-0.5f).add(position);
        return new RenderGrid(getSubGrid(corner, renderGridWidth, renderGridHeight));
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
        return new Grid(name + " " + corner + " " + width + " " + height,
                getSubTileGrid(corner, width, height), getSubEntityMap(corner, width, height));
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public Tile getTile(Vector2 position) {
        return tileArray[cast(position.x)][cast(position.y)];
    }

    public Tile[][] getTileGrid() {
        return tileArray;
    }

    public void clearTiles() {
        tileArray = new Tile[gridWidth][gridHeight];
    }

    public Map<Vector2, Entity> getEntityMap() {
        return entityMap;
    }

    public Collection<Entity> getEntities() {
        return entityMap.values();
    }

    public void clearEntities() {
        entityMap.clear();
    }

    public void clearAll() {
        clearTiles();
        clearEntities();
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Grid)) return false;

        Grid grid = (Grid) o;

        return Arrays.equals(this.getTileGrid(), grid.getTileGrid());
    }

    protected Tile[][] getSubTileGrid(Vector2 corner, int width, int height) {
        Tile[][] subTileArray = new Tile[width][height];
        for (int i=0; i<width; i++) {
            for (int j=0; j<height; j++) {
                Vector2 position = new Vector2(i, j).add(corner);
                if (isInBounds(position)) {
                    subTileArray[i][j] = tileArray[cast(position.x)][cast(position.y)];
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

    protected boolean isInBounds(int x, int y) {
        return ((x < gridWidth)
                && (x >= 0)
                && (y < gridHeight)
                && (y >= 0));
    }

    protected boolean isInBounds(Vector2 position) {
        return isInBounds(cast(position.x), cast(position.y));
    }

    protected boolean isPathable(Vector2 position) {
        return (isInBounds(position)
                && (getEntity(position) == null)
                && getTile(position).isPathable());
    }

    protected static int cast(float f) {
        return (int) f;
    }
}
