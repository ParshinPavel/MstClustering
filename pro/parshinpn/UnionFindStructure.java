package pro.parshinpn;

/**
 * Класс {@code UnionFindStructure} представляет реализацию
 * <em>union-find data type</em> (известную также как <em>disjoint-sets data type</em>).
 *
 * @author Паршин Павел.
 */
public class UnionFindStructure {
	/**
	 * Количество непересекающихся множеств.
	 */
	private int count;

	/**
	 * Массив идентификаторов непересекающихся множеств.
	 * Индекс массива соответствует объекту, значение -
	 * номеру множества, которому принадлежит данный объект.
	 */
	private int[] unions;

	/**
	 * <p>Инициализация структуры данных с начальным числом
	 * {@code objectsCount} объектов (от 0 до {@code objectsCount - 1}).</p>
	 * <p>Каждый объект представляет одно множество.</p>
	 *
	 * @param objectsCount количество объектов при инициализации.
	 * @throws IllegalArgumentException если {@code objectsCount < 0}.
	 */
	public UnionFindStructure(int objectsCount) {
		if (objectsCount < 0) {
			final String message = String.format("Инициализация системы непересекающихся множеств отрицательным значением недопустима (objectsCount = %d).", objectsCount);
			throw new IllegalArgumentException(message);
		}

		count = objectsCount;
		unions = new int[objectsCount];
		for (int objectIndex = 0; objectIndex < objectsCount; ++objectIndex) {
			// При инициализации каждый объект представляет отдельное множество.
			unions[objectIndex] = objectIndex;
		}
	}

	/**
	 * Возвращает идентификатор множества, в котором
	 * расположен объект {@code p}.
	 *
	 * @param p объект, для которого осуществляется поиск множества.
	 *
	 * @return идентификатор множества, в котором расположен объект {@code p}.
	 * @throws ArrayIndexOutOfBoundsException если {@code p} выходит за пределы
	 * диапазона {@code [0; objectsCount)}.
	 */
	public int find(int p) {
		return unions[p];
	}

	/**
	 * Соединяет множество, содержащее объект {@code p},
	 * со множеством, содержащим {@code q}.
	 *
	 * @param p идентификатор объекта из первого множества.
	 * @param q идентификатор объекта из другого множества.
	 *
	 * @throws ArrayIndexOutOfBoundsException если {@code p} или {@code q}
	 * выходит за пределы диапазона {@code [0; objectsCount)}.
	 */
	public void union(int p, int q) {
		final int pid = unions[p];
		final int qid = unions[q];
		for (int i = 0; i < unions.length; ++i) {
			if (unions[i] == pid) {
				unions[i] = qid;
			}
		}
		--count;
	}

	/**
	 * Возвращает количество непересекающихся множеств.
	 *
	 * @return количество непересекающихся множеств диапазоне от
	 * {@code 0} до {@code objectsCount - 1}.
	 */
	public int count() {
		return count;
	}

	/**
	 * Проверяет расположены ли объекты {@code p} и
	 * {@code q} в одном множестве.
	 *
	 * @param p первый объект.
	 * @param q второй объект.
	 *
	 * @return {@code true}, если оба объекта расположены в одном множестве,
	 * иначе {@code false}.
	 */
	public boolean connected(int p, int q) {
		return find(p) == find(q);
	}
}
