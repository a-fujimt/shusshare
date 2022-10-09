package net.akichil.shusshare.repository;

import net.akichil.shusshare.ShusshareApplication;
import net.akichil.shusshare.entity.*;
import net.akichil.shusshare.repository.dbunitUtil.DbTestExecutionListener;
import net.akichil.shusshare.repository.dbunitUtil.DbUnitUtil;
import net.akichil.shusshare.repository.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = ShusshareApplication.class)
public class RecruitmentRepositoryImplTest {

    @Autowired
    private RecruitmentRepository target;

    @Autowired
    private DataSource dataSource;

    @TestExecutionListeners({DbTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
    @Nested
    public class FindTest {

        @Test
        public void testFindAll() {
            final Integer accountId = 2;

            List<RecruitmentDetail> results = target.findList(accountId);

            assertEquals(2, results.size());
            assertEquals(3, results.get(1).getParticipants().size());
            assertEquals(4, results.get(0).getParticipants().get(0).getAccountId());
            assertEquals(3, results.get(0).getRecruitmentId());
            assertEquals("test_user4", results.get(0).getCreatedFriend().getUserId());
        }

        @Test
        public void testFindOne() {
            final Integer recruitmentId = 2;
            final Integer accountId = 1;

            RecruitmentDetail result = target.findOne(recruitmentId, accountId);

            assertEquals("募集byふが", result.getTitle());
            assertEquals("ふが山フガ子", result.getCreatedFriend().getUserName());
            assertEquals(0, result.getParticipants().size());
        }

        @Test
        public void testFindOneNotFollowed() {
            final Integer recruitmentId = 4;
            final Integer accountId = 1;

            RecruitmentDetail result = target.findOne(recruitmentId, accountId);

            assertEquals("募集テスト4", result.getTitle());
            assertEquals(FriendStatus.NONE, result.getCreatedFriend().getStatus());
        }

    }


    @TestExecutionListeners({DbTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
    @Nested
    public class InsertTest {

        private static final String INSERT_DATA_PATH = "src/test/resources/testdata/insert";

        /**
         * 募集を新規追加
         */
        @Test
        public void testInsertRecruitment() throws Exception {
            Recruitment recruitment = Recruitment.builder()
                    .title("募集テスト1-2")
                    .createdBy(1)
                    .genre(RecruitmentGenre.CAFE)
                    .capacity(3)
                    .deadline(LocalDateTime.of(2022, 6, 5, 16, 0))
                    .participantCount(0)
                    .shusshaId(1)
                    .status(RecruitmentStatus.OPENED)
                    .build();

            target.add(recruitment);

            DbUnitUtil.assertMutateResults(dataSource, "recruitment", INSERT_DATA_PATH,
                    "recruitment_id", "updated_at");
        }
    }

    @TestExecutionListeners({DbTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
    @Nested
    public class UpdateTest {

        private static final String UPDATE_DATA_PATH = "src/test/resources/testdata/update";

        /**
         * 募集を更新
         */
        @Test
        public void testUpdateRecruitment() throws Exception {
            Recruitment recruitment = Recruitment.builder()
                    .recruitmentId(3)
                    .title("募集3-更新")
                    .capacity(4)
                    .deadline(LocalDateTime.of(2022, 6, 6, 13, 0))
                    .lockVersion(0)
                    .build();

            target.set(recruitment);

            DbUnitUtil.assertMutateResults(dataSource, "recruitment", UPDATE_DATA_PATH,
                    "updated_at");
        }

        /**
         * 募集の更新に失敗（指定されたIDなし）
         */
        @Test
        public void testUpdateRecruitmentFailByIdNotFound() {
            Recruitment recruitment = Recruitment.builder()
                    .recruitmentId(6)
                    .title("募集x-更新")
                    .lockVersion(0)
                    .build();

            assertThrows(ResourceNotFoundException.class, () -> target.set(recruitment));
        }
    }

    @TestExecutionListeners({DbTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
    @Nested
    public class DeleteTest {

        private static final String DELETE_DATA_PATH = "src/test/resources/testdata/delete";

        /**
         * 募集を削除
         */
        @Test
        public void testDeleteRecruitment() throws Exception {
            final Integer recruitment = 1;

            target.remove(recruitment);

            DbUnitUtil.assertMutateResults(dataSource, "recruitment", DELETE_DATA_PATH,
                    "updated_at");
        }

        /**
         * 募集の削除に失敗（指定されたIDなし）
         */
        @Test
        public void testDeleteRecruitmentFailByIdNotFound() {
            final Integer recruitmentId = 10;

            assertThrows(ResourceNotFoundException.class, () -> target.remove(recruitmentId));
        }
    }

}
