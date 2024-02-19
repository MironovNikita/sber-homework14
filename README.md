
# java.util.concurrent

### ✨Немного теории

🛠️ **java.util.concurrent** - это пакет в языке **`Java`**, который предоставляет широкий набор удобных инструментов и структур данных для работы с параллельным программированием и многопоточностью. Он введен начиная с версии **`Java 5`** и был значительно расширен в последующих версиях.

🔧 Вот несколько основных классов и интерфейсов из **java.util.concurrent**:
1. ***`Executor и ExecutorService`***. Эти интерфейсы предоставляют абстракцию для выполнения задач в фоновом режиме в отдельном потоке. **Executor** предоставляет метод `execute()`, принимающий **Runnable**, в то время как **ExecutorService** предоставляет более расширенные возможности, такие как управление потоками и выполнение задач с возвратом результата.

2. ***`ThreadPoolExecutor`***. Это реализация интерфейса **ExecutorService**, которая представляет собой гибкий пул потоков для выполнения задач. Он позволяет настраивать параметры, такие как размер пула, задержку перед запуском новых задач и т.д.

***`Future и FutureTask`***. **Future** представляет результат асинхронной вычислительной задачи и позволяет управлять её выполнением. **FutureTask** - это реализация **Future**, которая также может быть использована как **Runnable**, и может быть выполнена в пуле потоков.

***`CompletionService`***. Этот интерфейс представляет сервис, который принимает задачи для выполнения и возвращает результаты по мере их завершения. Это удобный способ обрабатывать результаты асинхронных задач по мере их завершения.

***`Lock и ReentrantLock`***. Эти интерфейсы предоставляют механизмы для синхронизации доступа к общим ресурсам в многопоточных приложениях. Они позволяют более гибко управлять блокировками и избежать проблем с блокировками и взаимной блокировкой.

***`ConcurrentHashMap`***. Это реализация интерфейса **Map**, которая является потокобезопасной и эффективной для параллельного доступа. Она обеспечивает высокую производительность при операциях чтения и записи в многопоточной среде.

***`ConcurrentLinkedQueue`***. Это реализация интерфейса **Queue**, предназначенная для использования в многопоточных приложениях. Она обеспечивает эффективную вставку и удаление элементов в очереди при параллельном доступе.

Пакет предоставляет обширный набор инструментов для разработки многопоточных приложений с повышенной производительностью, безопасностью и эффективностью.

### 🚀Практика

В данной работе представлена реализация заданий, связанных с вышеописанной темой:
1. Реализация класса `Task`, взаимодействующего с `Callable`
2. Реализация интерфейса `ExecutionManager`

## Задание 1

Ваша задача реализовать класс **`Task`** имеющий один метод `get()`:

```java
public class Task<T> {
    …
 
    public Task(Callable<? extends T> callable) {
        //...
     }
 
    public T get() {
         … // todo implement me
    }
}
```
 
Данный класс в конструкторе принимает экземпляр `java.util.concurrent.Callable`. **`Callable`** похож на **`Runnable`**, но результатом его работы является объект (а не void).
 
Ваша задача реализовать метод `get()` который возвращает результат работы **`Callable`**. Выполнение **callable** должен начинать тот поток, который первый вызвал метод `get()`. Если несколько потоков одновременно вызывают этот метод, то выполнение должно начаться только в _одном_ потоке, а остальные должны ожидать конца выполнения (не нагружая процессор).

Если при вызове `get()` результат уже просчитан, то он должен вернуться сразу, (даже без задержек на вход в синхронизированную область). 

Если при просчёте результата произошел **Exception**, то всем потокам при вызове `get()`, надо кидать этот **Exception**, обернутый в ваш `RuntimeException` (подходящее название своему ексепшену придумайте сами).

## Описание результатов

🤔 Для реализации данного задания был создан класс [**`Task`**](https://github.com/MironovNikita/sber-homework14/blob/main/src/main/java/org/application/Task/Task.java), который представляет собой обёртку над объектом типа **`Callable`**. Он аналогичен **`Runnable`**, но может возвращать результат выполнения. 

Если при просчёте результата возникнет ошибка, то все потоки будут выбрасывать [**`CustomTaskException`**](https://github.com/MironovNikita/sber-homework14/blob/main/src/main/java/org/application/Task/CustomTaskException.java). 

🚀 Для тестового запуска задания был создан класс [**`TaskApp`**](https://github.com/MironovNikita/sber-homework14/blob/main/src/main/java/org/application/Task/TaskApp.java). 

В данном классе создаётся экземпляр класса [**`Task`**](https://github.com/MironovNikita/sber-homework14/blob/main/src/main/java/org/application/Task/Task.java), который выполняет задачу в метоже `get()`: имитация длительной работы `TimeUnit.SECONDS.sleep(5)` и возвращение значения _5_.

Далее создаётся `ExecutorService` с пулом потоков из 5 штук. Трижды производится вызов метода `submit()` для выполнения задачи из объекта [**`Task`**](https://github.com/MironovNikita/sber-homework14/blob/main/src/main/java/org/application/Task/Task.java). Каждый подобный вызов возвращает объект `Future`, который представляет результат асинхронной операции.

После того, как все задачи на выполнение были отправлены, производится остановка пула потоков методом `shutdown()`. Далее происходит ожидание завершения каждой задачи с помощью вызова метода `get()` для каждого объекта **Future**. Если при вызове `get()` произошло исключение типа `InterruptedException` или `ExecutionException`, оно будет перехвачено и выведено на консоль. После того как все результаты были получены, проверяется, одинаковы ли они. 

Результат запуска задания:

![task1](https://github.com/MironovNikita/sber-homework14/blob/main/res/task1.png)

## Задание 2

Ваша задача реализовать интерфейс **`ExecutionManager`**
```java
public interface ExecutionManager {
    Context execute(Runnable callback, Runnable... tasks);
}
```
 
Метод `execute` принимает массив тасков - это задания которые **`ExecutionManager`** должен выполнять параллельно (в вашей реализации пусть будет в своем пуле потоков). После завершения всех тасков должен выполниться `callback` (ровно 1 раз). 
 
Метод `execute` – это неблокирующий метод, который сразу возвращает объект **`Context`**. **`Context`** это интерфейс следующего вида:
```java
public interface Context {
    int getCompletedTaskCount();
 
    int getFailedTaskCount();
 
    int getInterruptedTaskCount();
 
    void interrupt();
 
    boolean isFinished();
}
```
Описание интерфейса:
- метод `getCompletedTaskCount()` возвращает количество тасков, которые на текущий момент успешно выполнились.
- метод `getFailedTaskCount()` возвращает количество тасков, при выполнении которых произошел `Exception`.
- метод `interrupt()` отменяет выполнения тасков, которые еще не начали выполняться.
- метод `getInterruptedTaskCount()` возвращает количество тасков, которые не были выполены из-за отмены (вызовом предыдущего метода).
- метод `isFinished()` вернет true, если все таски были выполнены или отменены, false в противном случае.  

## Описание результатов

🤨 Для реализации данного задания были созданы интерфейсы 

[**`ExecutionManager`**](https://github.com/MironovNikita/sber-homework14/blob/main/src/main/java/org/application/ExecutionManager/ExecutionManager.java):

```java
public interface ExecutionManager {
    Context execute(Runnable callback, Runnable... tasks);
}
```
и [**`Context`**](https://github.com/MironovNikita/sber-homework14/blob/main/src/main/java/org/application/ExecutionManager/Context.java):
```java
public interface Context {
    int getCompletedTaskCount();

    int getFailedTaskCount();

    int getInterruptedTaskCount();

    void interrupt();

    boolean isFinished();
}
```

Затем был создан класс [**`ExecutionManagerImpl`**](https://github.com/MironovNikita/sber-homework14/blob/main/src/main/java/org/application/ExecutionManager/ExecutionManagerImpl.java). Данный класс реализует интерфейс [**`ExecutionManager`**](https://github.com/MironovNikita/sber-homework14/blob/main/src/main/java/org/application/ExecutionManager/ExecutionManager.java) и предоставляет реализацию метода `execute()`, который запускает выполнение задач в потоках и возвращает объект контекста выполнения.

В конструкторе класса мы создаём пул потоков **`ExecutorService`**. В методе `execute(Runnable callback, Runnable... tasks)` создаётся объект **`ExecutionContext`**, который представляет собой контекст выполнения всех задач. Для каждой задачи вызывается метод `submit()`, в котором происходит уведомление **`ExecutionContext`** об успешности выполнения задачи. В случае положительного вызывается `onTaskCompleted()`, в случае отрицательного - `onTaskFailed()`.

Сам **`ExecutionContext`** является вложенным классом, реализующим интерфейс [**`Context`**](https://github.com/MironovNikita/sber-homework14/blob/main/src/main/java/org/application/ExecutionManager/Context.java). В данном контексте хранится информация о количестве выполненных, завершившихся с ошибкой и прерванных задач, а также о состоянии выполнения всего набора задач. 

Для проверки корректности работы 2го задания был создан класс [**`ExecutionApp`**](https://github.com/MironovNikita/sber-homework14/blob/main/src/main/java/org/application/ExecutionManager/ExecutionApp.java). В данном классе мы сооздаём объект класса **`ExecutionManagerImpl`**, `callback`, который выполнится в самом конце, после завершения всех задач, а также создаётся массив задач `tasks`, каждая из которых представляет собой лямбда-выражение, имитирующее некоторую работу. Для имитации ошибок выполнения каждая чётная задача выбрасывает исключение с выводом сообщения об ошибке в консоль.

Затем вызывается метод `execute(callback, tasks)`, который передаёт задачи на выполнение и возвращает объект контекста этого выполнения. Затем главный поток ожидает три секунды, а после вызова `interrupt()` контекста выполнения осуществляется прерывание выполнения всех оставшихся задач.

После осуществляется проверка, что все задачи контекста завершены, с последующим выводом статистики об успешных, задачах, завершившихся с ошибкой, и прерванных задачах.

Результат запуска задания:

![task2](https://github.com/MironovNikita/sber-homework14/blob/main/res/task2.png)

### 💡 Примечание

В [**`pom.xml`**](https://github.com/MironovNikita/sber-homework14/blob/main/pom.xml) данного задания не добавлялись зависимости в блоке *properties /properties*:

Результат сборки текущего проекта:

```java
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.558 s
[INFO] Finished at: 2024-02-19T19:53:18+03:00
[INFO] ------------------------------------------------------------------------
```