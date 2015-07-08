package saltybot.api.store;

import saltybot.api.common.Outcome;
import saltybot.api.common.Victor;

import java.io.*;
import java.nio.ByteBuffer;

public class FileStore implements AutoCloseable, DataStore {

    private static final int PLAYER_NAME_SIZE = Character.BYTES * 512;
    private static final int PLAYER_POOL_SIZE = Double.BYTES;
    private static final int PLAYER_VICTORY_SIZE = Byte.BYTES;
    private static final int BUFFER_SIZE = PLAYER_NAME_SIZE * 2 + PLAYER_POOL_SIZE * 2 + PLAYER_VICTORY_SIZE;

    private static final int PLAYER_ONE_NAME_OFFSET = 0;
    private static final int PLAYER_TWO_NAME_OFFSET = PLAYER_ONE_NAME_OFFSET + PLAYER_NAME_SIZE;
    private static final int PLAYER_ONE_BET_OFFSET = PLAYER_TWO_NAME_OFFSET + PLAYER_NAME_SIZE;
    private static final int PLAYER_TWO_BET_OFFSET = PLAYER_ONE_BET_OFFSET + PLAYER_POOL_SIZE;
    private static final int PLAYER_VICTORY_OFFSET = PLAYER_TWO_BET_OFFSET + PLAYER_POOL_SIZE;

    private final OutputStream outputStream;

    public FileStore() throws FileNotFoundException {
        outputStream = new FileOutputStream("match.dat");
    }

    public void write(final Outcome outcome) {
        try {
            final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

            buffer.position(PLAYER_ONE_NAME_OFFSET);
            buffer.put(outcome.getPlayerOneName().getBytes());

            buffer.position(PLAYER_TWO_NAME_OFFSET);
            buffer.put(outcome.getPlayerTwoName().getBytes());

            buffer.position(PLAYER_ONE_BET_OFFSET);
            buffer.putDouble(outcome.getPlayerOneBetPool());

            buffer.position(PLAYER_TWO_BET_OFFSET);
            buffer.putDouble(outcome.getPlayerTwoBetPool());

            buffer.position(PLAYER_VICTORY_OFFSET);
            buffer.put(translateVictor(outcome.getVictor()));

            try {
                outputStream.write(buffer.array());
            } finally {
                outputStream.flush();
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private byte translateVictor(final Victor victor) {
        switch (victor) {
            case PLAYER_ONE: return 1;
            case PLAYER_TWO: return 2;
            default: return 0;
        }
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }
}
