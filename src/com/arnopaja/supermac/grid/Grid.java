package com.arnopaja.supermac.grid;

import com.arnopaja.supermac.objects.Entity;
import com.arnopaja.supermac.objects.Tile;
import com.badlogic.gdx.math.Vector2;

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

    protected final int gridWidth, gridHeight;

    protected Tile[][] tileArray;
    protected Map<Vector2, Entity> entityMap;

    public Grid(int gridWidth, int gridHeight) {
        this(new Tile[gridWidth][gridHeight]);
    }

    public Grid(Tile[][] tileArray) {
        this(tileArray, new Hashtable<Vector2, Entity>());
    }

    public Grid(Tile[][] tileArray, Map<Vector2, Entity> entityMap) {
        this.tileArray = tileArray;
        this.entityMap = entityMap;
        gridWidth = tileArray.length;
        gridHeight = tileArray[0].length;
    }

    public Grid(Grid grid) {
        this(grid.getTileGrid(), grid.getEntityMap());
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public Tile[][] getTileGrid() {
        return tileArray;
    }

    public Map<Vector2, Entity> getEntityMap() {
        return entityMap;
    }

    public Collection<Entity> getEntities() {
        return entityMap.values();
    }

    public Tile getTile(Vector2 position) {
        return tileArray[floor(position.x)][floor(position.y)];
    }

    public void setTile(Vector2 position, Tile element) {
        tileArray[floor(position.x)][floor(position.y)] = element;
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
     * Puts the entity in the map at new coordinates.
     *
     * @param entity the entity to be placed in the map
     * @param newPosition the new position
     * @return the entity previously at the new location
     */
    public Entity putEntity(Entity entity, Vector2 newPosition) {
        entity.setPosition(newPosition);
        return putEntity(entity);
    }

    /**
     * Moves the entity at the specified coordinates in the specified direction.
     *
     * @param position the position of the entity
     * @param dir the direction in which to move
     * @return true if an entity was moved, else false
     */
    public boolean moveEntity(Vector2 position, Direction dir) {
        return moveEntity(getEntity(position), dir);
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
            if (isInBounds(destination)
                    && (getEntity(destination) == null)
                    && getTile(destination).isPathable()) {
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

    /**
     * Duplicate of the other getSubTileGrid method using a Vector2 instead of x and y coords.
     *
     * @param corner the coordinates of upper left hand corner of the sub tile array.
     *                 can have negative values
     * @param width the width of the sub tile array
     * @param height the height of the sub tile array
     * @return the sub tile array
     */
    public Tile[][] getSubTileGrid(Vector2 corner, int width, int height) {
        Tile[][] subTileArray = new Tile[width][height];
        for (int i=0; i<width; i++) {
            for (int j=0; j<height; j++) {
                Vector2 position = new Vector2(i, j).add(corner);
                if (isInBounds(position)) {
                    subTileArray[i][j] = tileArray[floor(position.x)][floor(position.y)];
                } else {
                    subTileArray[i][j] = Tile.createNullTile();
                }
            }
        }
        return subTileArray;
    }

    /**
     * Returns a map of Entities that corresponds to the described subgrid.
     *
     * @param position the coordinates of upper left hand corner of the sub entity map.
     *                 can have negative values
     * @param width the width of the sub entity map
     * @param height the height of the sub entity map
     * @return the sub entity map
     */
    public Map<Vector2, Entity> getSubEntityMap(Vector2 position, int width, int height) {
        Map<Vector2, Entity> subEntityMap = new Hashtable<Vector2, Entity>();
        for (int i=0; i<width; i++) {
            for (int j=0; j<height; j++) {
                Vector2 newKey = new Vector2(i, j);
                Vector2 key = newKey.cpy().add(position);
                if (entityMap.containsKey(key)) {
                    subEntityMap.put(newKey, getEntity(key));
                }
            }
        }
        return subEntityMap;
    }

    /**
     * Returns a Grid object whose tile grid is determined by the getSubTileGrid method,
     * and whose entity map is determined by the getSubEntityMap method. The subgrid has
     * its upper left hand corner at the specified position, and its width and height
     * specified by those parameters.
     *
     * @param position the coordinates of upper left hand corner of the subgrid.  Can be negative
     * @param width the width of the subgrid
     * @param height the height of the subgrid
     * @return the subgrid
     */
    public Grid getSubGrid(Vector2 position, int width, int height) {
        return new Grid(getSubTileGrid(position, width, height),
                getSubEntityMap(position, width, height));
    }

    /**
     * Returns a RenderGrid of the calling grid centered at the given position.
     * If the RenderGrid covers spaces not in the calling grid, those spaces will be
     * set as blank elements.
     *
     *
     * @param position the coordinates of the center of the RenderGrid
     * @param renderGridWidth the width of the RenderGrid
     * @param renderGridHeight the height of the RenderGrid
     * @return the RenderGrid
     */
    public RenderGrid getRenderGrid(Vector2 position, float renderGridWidth, float renderGridHeight) {
        Vector2 corner = new Vector2(renderGridWidth, renderGridHeight)
                .add(-1, -1).scl(-0.5f).add(position);
        return new RenderGrid(getSubGrid(corner, ceil(renderGridWidth), ceil(renderGridHeight)));
    }

    protected static int ceil(float f) {
        return (int) Math.ceil(f);
    }

    protected static int floor(float f) {
        return (int) Math.floor(f);
    }

    public static Vector2 floor(Vector2 v) {
        return new Vector2((int) v.x, (int) v.y);
    }

    protected boolean isInBounds(int x, int y) {
        return ((x < gridWidth)
                && (x >= 0)
                && (y < gridHeight)
                && (y >= 0));
    }

    protected boolean isInBounds(Vector2 position) {
        return isInBounds(floor(position.x), floor(position.y));
    }

    public void clearTiles() {
        tileArray = new Tile[gridWidth][gridHeight];
    }

    public void clearEntities() {
        entityMap.clear();
    }

    public void clearAll() {
        clearTiles();
        clearEntities();
    }
}