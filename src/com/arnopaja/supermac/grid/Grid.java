package com.arnopaja.supermac.grid;

import com.arnopaja.supermac.helpers.AssetLoader;
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

    public Tile getTile(int x, int y) {
        return tileArray[x][y];
    }

    public Tile getTile(Vector2 position) {
        return getTile((int) position.x, (int) position.y);
    }

    public void setTile(int x, int y, Tile element) {
        tileArray[x][y] = element;
    }

    public void setTile(Vector2 position, Tile element) {
        setTile((int) position.x, (int) position.y, element);
    }

    /**
     * Returns the entity at the specified coordinates,
     * or null if no entity exists there.
     *
     * @param x the x coordinate of the entity
     * @param y the y coordinate of the entity
     * @return the entity at the specified coordinates, or null
     */
    public Entity getEntity(int x, int y) {
        return getEntity(new Vector2(x, y));
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
     * @param newX the new x coordinate
     * @param newY the new y coordinate
     * @return the entity previously at the new location
     */
    public Entity putEntity(Entity entity, int newX, int newY) {
        return putEntity(entity, new Vector2(newX, newY));
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
     * @param x the x coordinate of the entity
     * @param y the y coordinate of the entity
     * @param dir the direction in which to move
     * @return true if an entity was moved, else false
     */
    public boolean moveEntity(int x, int y, Direction dir) {
        return moveEntity(new Vector2(x, y), dir);
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
     * Removes the entity at the specified coordinates.
     * Returns the entity removed, or null if there was no entity there.
     *
     * @param x the x coordinate of the position
     * @param y the y coordinate of the position
     * @return the entity removed, or null if there was no entity there
     */
    public Entity removeEntity(int x, int y) {
        return removeEntity(new Vector2(x, y));
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
     * Returns an array of Tiles that corresponds to the described subgrid.
     * If the described subgrid covers area outside the caller's grid array, the
     * missing elements are set as null tiles.
     *
     * @param x the x coordinate of upper left hand corner of the sub tile array.  Can be negative
     * @param y the y coordinate of upper left hand corner of the sub tile array.  Can be negative
     * @param width the width of the sub tile array
     * @param height the height of the sub tile array
     * @return the sub tile array
     */
    public Tile[][] getSubTileGrid(int x, int y, int width, int height) {
        Tile[][] subTileArray = new Tile[width][height];
        for (int i=0; i<width; i++) {
            for (int j=0; j<height; j++) {
                if (isInBounds(x+i, y+j)) {
                    subTileArray[i][j] = tileArray[i+x][j+y];
                } else {
                    subTileArray[i][j] = Tile.createNullTile();
                }
            }
        }
        return subTileArray;
    }

    /**
     * Duplicate of the other getSubTileGrid method using a Vector2 instead of x and y coords.
     *
     * @param position the coordinates of upper left hand corner of the sub tile array.
     *                 can have negative values
     * @param width the width of the sub tile array
     * @param height the height of the sub tile array
     * @return the sub tile array
     */
    public Tile[][] getSubTileGrid(Vector2 position, int width, int height) {
        return getSubTileGrid((int) position.x, (int) position.y, width, height);
    }

    /**
     * Returns a map of Entities that corresponds to the described subgrid.
     *
     * @param x the x coordinate of upper left hand corner of the sub entity map.  Can be negative
     * @param y the y coordinate of upper left hand corner of the sub entity map.  Can be negative
     * @param width the width of the sub entity map
     * @param height the height of the sub entity map
     * @return the sub entity map
     */
    public Map<Vector2, Entity> getSubEntityMap(int x, int y, int width, int height) {
        Map<Vector2, Entity> subEntityMap = new Hashtable<Vector2, Entity>();
        for (int i=0; i<width; i++) {
            for (int j=0; j<height; j++) {
                Vector2 key = new Vector2(i+x, j+y);
                if (entityMap.containsKey(key)) {
                    subEntityMap.put(new Vector2(i, j), getEntity(key));
                }
            }
        }
        return subEntityMap;

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
        return getSubEntityMap((int) position.x, (int) position.y, width, height);
    }

    /**
     * Returns a Grid object whose tile grid is determined by the getSubTileGrid method,
     * and whose entity map is determined by the getSubEntityMap method. The subgrid has
     * its upper left hand corner at the specified coordinates, and its width and height
     * specified by those parameters.
     *
     * @param x the x coordinate of upper left hand corner of the subgrid.  Can be negative
     * @param y the y coordinate of upper left hand corner of the subgrid.  Can be negative
     * @param width the width of the subgrid
     * @param height the height of the subgrid
     * @return the subgrid
     */
    public Grid getSubGrid(int x, int y, int width, int height) {
        return new Grid(getSubTileGrid(x, y, width, height), getSubEntityMap(x, y, width, height));
    }

    /**
     * Returns a Grid object whose tile grid is determined by the getSubTileGrid method,
     * and whose entity map is determined by the getSubEntityMap method. The subgrid has
     * its upper left hand corner at the specified position, and its width and height
     * specified by those parameters.
     *
     *
     * @param position the coordinates of upper left hand corner of the subgrid.  Can be negative
     * @param width the width of the subgrid
     * @param height the height of the subgrid
     * @return the subgrid
     */
    public Grid getSubGrid(Vector2 position, int width, int height) {
        return getSubGrid((int) position.x, (int) position.y, width, height);
    }

    /**
     * Returns a RenderGrid of the calling grid centered at the given coordinates.
     * If the RenderGrid covers spaces not in the calling grid, those spaces will be
     * set as blank elements.
     *
     * @param x the x coordinate of the center of the RenderGrid
     * @param y the x coordinate of the center of the RenderGrid
     * @return the RenderGrid
     */
    public RenderGrid getRenderGrid(int x, int y) {
        int cornerX = x - (RenderGrid.RENDER_WIDTH - 1)/2;
        int cornerY = y - (RenderGrid.RENDER_HEIGHT - 1)/2;
        return new RenderGrid(getSubGrid(cornerX, cornerY,
                RenderGrid.RENDER_WIDTH, RenderGrid.RENDER_HEIGHT));
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
        return getRenderGrid((int) position.x, (int) position.y);
    }

    public void fillTileGrid(String name) {
        tileArray = AssetLoader.parseTileArray(name);
    }

    protected boolean isInBounds(int x, int y) {
        return ((x < gridWidth)
                && (x >= 0)
                && (y < gridHeight)
                && (y >= 0));
    }

    protected boolean isInBounds(Vector2 position) {
        return isInBounds((int) position.x, (int) position.y);
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
