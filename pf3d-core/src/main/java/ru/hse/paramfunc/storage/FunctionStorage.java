package ru.hse.paramfunc.storage;

import ru.hse.paramfunc.domain.Function;
import ru.hse.paramfunc.listener.Listener;
import ru.hse.paramfunc.listener.Notifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FunctionStorage implements Notifier {

    private static final FunctionStorage instance = new FunctionStorage();

    private final List<Function> functionList;

    private FunctionStorage() {
        functionList = new ArrayList<>();
    }

    public static FunctionStorage getInstance() {
        return instance;
    }

    public void addFunction(Function function) {
        functionList.add(function);
        notifyListeners();
    }

    public void removeFunction(Function function) {
        functionList.remove(function);
        notifyListeners();
    }

    public List<Function> getFunctions() {
        return List.copyOf(functionList);
    }

    @Override
    public void notifyListeners() {
        // Оповещаем подписчиков в обратном порядке, чтобы контроллер узнал о событии последним
        List<Listener> copyListeners = new ArrayList<>(listeners);
        Collections.reverse(copyListeners);
        copyListeners.forEach(Listener::receive);
    }

}
