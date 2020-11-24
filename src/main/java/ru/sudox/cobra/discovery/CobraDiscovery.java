package ru.sudox.cobra.discovery;

import ru.sudox.cobra.CobraLoader;
import ru.sudox.cobra.discovery.exceptions.*;

import static ru.sudox.cobra.discovery.CobraDiscoveryErrors.*;

public final class CobraDiscovery {

    private final long pointer;
    private CobraDiscoveryListener listener;

    public CobraDiscovery() {
        this.pointer = create(CobraLoader.getPointer());
    }

    private void onFound(String host) {
        if (listener != null) {
            listener.onFound(this, host);
        }
    }

    private void onClose(int error) {
        if (listener != null) {
            switch (error) {
                case OK -> listener.onClose(this, null);
                case ALREADY_STARTED_ERROR -> listener.onClose(null, new CobraDiscoveryAlreadyStartedException());
                case BINDING_ERROR -> listener.onClose(this, new CobraDiscoveryBindingException());
                case JOINING_GROUP_ERROR -> listener.onClose(this, new CobraDiscoveryJoiningGroupException());
                case SENDING_ERROR -> listener.onClose(this, new CobraDiscoverySendingException());
                default -> listener.onClose(this, new CobraDiscoveryUnhandledException(error));
            }
        }
    }

    public void scan() throws CobraDiscoveryAlreadyStartedException, CobraDiscoveryBindingException, CobraDiscoveryJoiningGroupException, CobraDiscoverySendingException, CobraDiscoveryUnhandledException {
        handleResult(scan(pointer));
    }

    public void listen() throws CobraDiscoveryAlreadyStartedException, CobraDiscoveryBindingException, CobraDiscoveryJoiningGroupException, CobraDiscoverySendingException, CobraDiscoveryUnhandledException {
        handleResult(listen(pointer));
    }

    public void close() throws CobraDiscoveryUnhandledException, CobraDiscoveryAlreadyStartedException, CobraDiscoveryJoiningGroupException, CobraDiscoveryBindingException, CobraDiscoverySendingException {
        handleResult(close(pointer));
    }

    private void handleResult(int res) throws CobraDiscoveryAlreadyStartedException, CobraDiscoveryBindingException, CobraDiscoveryJoiningGroupException, CobraDiscoverySendingException, CobraDiscoveryUnhandledException {
        if (res != OK) {
            switch (res) {
                case ALREADY_STARTED_ERROR -> throw new CobraDiscoveryAlreadyStartedException();
                case BINDING_ERROR -> throw new CobraDiscoveryBindingException();
                case JOINING_GROUP_ERROR -> throw new CobraDiscoveryJoiningGroupException();
                case SENDING_ERROR -> throw new CobraDiscoverySendingException();
                default -> throw new CobraDiscoveryUnhandledException(res);
            }
        }
    }

    public void setListener(CobraDiscoveryListener listener) {
        this.listener = listener;
    }

    @Override
    protected void finalize() {
        destroy(pointer);
    }

    private static native void destroy(long pointer);

    private static native long create(long loaderPointer);

    private static native int close(long pointer);

    private native int listen(long pointer);

    private native int scan(long pointer);
}