package utils;

public interface IRepository<TEntity> {
    void add(TEntity entity);
    TEntity get(int id);
    void update(int id, TEntity entity);
    void delete(int id);
}
