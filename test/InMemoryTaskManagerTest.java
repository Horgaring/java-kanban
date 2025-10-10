import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void setUp() {
        taskManger = (InMemoryTaskManager) Managers.getDefault();
    }

}
