# krsv
# RxJava Lite (упрощённая реализация)

Минималистичная реализация реактивного программирования на Java с основными операторами и управлением потоками.

## 📦 Основные компоненты

- `Observable` - поток данных
- `Observer` - подписчик
- `map`, `filter`, `flatMap` - операторы преобразования
- `Scheduler` - управление потоками выполнения

## 🚀 Быстрый старт

```java
Observable.create(emitter -> {
    emitter.onNext(1);
    emitter.onNext(2);
    emitter.onComplete();
})
.map(x -> x * 10)
.filter(x -> x > 15)
.subscribe(new Observer<Integer>() {
    @Override
    public void onNext(Integer item) {
        System.out.println("Получено: " + item);
    }

    @Override
    public void onError(Throwable error) {
        System.err.println("Ошибка: " + error.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println("Завершено!");
    }
});

### Ключевые компоненты:
- **Schedulers**: Управление потоками выполнения
- **Utils**: Вспомогательные инструменты
- **Tests**: Полное покрытие основных сценариев
## 📌 Особенности реализации

### 🔄 Реактивное ядро
- Чистая реализация паттерна **Observable/Observer**
- Ленивые вычисления (выполнение только при подписке)
- Каскадная обработка ошибок через `onError()`

### 🧩 Поддержка операторов
| Оператор  | Назначение                          | Пример использования          |
|-----------|-------------------------------------|-------------------------------|
| `map`     | Преобразование элементов            | `.map(x -> x * 2)`            |
| `filter`  | Фильтрация элементов                | `.filter(x -> x > 10)`        | 
| `flatMap` | Асинхронное преобразование          | `.flatMap(url -> fetch(url))` |

### 🧵 Управление потоками
```java
// Выполнение в IO-потоке
.subscribeOn(new IOThreadScheduler()) 

// Обработка в UI-потоке
.observeOn(new SingleThreadScheduler())
