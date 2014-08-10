package org.miles2run.business.services.jpa;

import org.hamcrest.CoreMatchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.miles2run.business.domain.jpa.*;
import org.miles2run.business.producers.EntityManagerProducer;
import org.miles2run.business.vo.ProfileDetails;
import org.miles2run.business.vo.ProfileGroupDetails;
import org.miles2run.business.vo.ProfileSocialConnectionDetails;
import org.miles2run.jaxrs.forms.ProfileForm;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.util.List;

/**
 * Created by shekhargulati on 10/08/14.
 */
@RunWith(Arquillian.class)
public class GoalJPAServiceTest {


    @Deployment
    public static Archive<?> deployment() {
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class).
                addClass(BaseEntity.class).
                addClass(Profile.class).
                addClass(SocialConnection.class).
                addClass(ProfileService.class).
                addClass(ProfileDetails.class).
                addClass(ProfileSocialConnectionDetails.class).
                addClass(SocialProvider.class).
                addClass(Role.class).
                addClass(Gender.class).
                addClass(ProfileForm.class).
                addClass(ProfileGroupDetails.class).
                addClass(Goal.class).
                addClass(GoalUnit.class).
                addClass(GoalType.class).
                addClass(Profile.class).
                addClass(EntityManagerProducer.class).
                addClass(GoalJPAService.class).
                addClass(ProfileService.class).
                addClass(CommunityRun.class).
                addClass(CommunityRunJPAService.class).
                addAsResource("META-INF/test_persistence.xml", "META-INF/persistence.xml").
                addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.printf("WebArchive %s", webArchive.toString(true));
        return webArchive;
    }

    @Inject
    private GoalJPAService goalJPAService;

    @Inject
    private ProfileService profileService;

    @Inject
    private EntityManager entityManager;
    @Inject
    private UserTransaction userTransaction;

    @Before
    public void setUp() throws Exception {
        userTransaction.begin();
        entityManager.createQuery("DELETE from Goal g").executeUpdate();
        entityManager.createQuery("DELETE from Profile p").executeUpdate();
        userTransaction.commit();
    }

    @Test
    public void findAllGoals_3ActiveGoalsAnd2ArchivedGoalCreatedByUser_ReturnGoalsCollectionWithSize3() throws Exception {
        Profile profile = createProfile();
        createNActiveGoals(profile, 3);
        createNArchivedGoals(profile, 2);
        List<Goal> activeGoals = goalJPAService.findAllGoals(profile, false);
        Assert.assertThat(activeGoals, IsCollectionWithSize.hasSize(3));
    }

    @Test
    public void countOfActiveGoalsCreatedByUser_3ActiveGoalsAnd2ArchivedGoalsCreatedByUser_ShouldBeThreeGoals() throws Exception {
        Profile profile = createProfile();
        createNActiveGoals(profile, 3);
        createNArchivedGoals(profile, 2);
        long count = goalJPAService.countOfActiveGoalCreatedByUser(profile);
        Assert.assertThat(count, CoreMatchers.is(CoreMatchers.equalTo(3L)));
    }

    @Test
    public void countOfActiveGoalsCreatedByUser_NoGoalCreatedByUser_Return0Goal() throws Exception {
        Profile profile = createProfile();
        long count = goalJPAService.countOfActiveGoalCreatedByUser(profile);
        Assert.assertThat(count, CoreMatchers.is(CoreMatchers.equalTo(0L)));
    }

    @Test
    public void countOfActiveGoalsCreatedByUser_2ArchivedGoalsCreatedByUser_Return0Goal() throws Exception {
        Profile profile = createProfile();
        createNArchivedGoals(profile, 2);
        long count = goalJPAService.countOfActiveGoalCreatedByUser(profile);
        Assert.assertThat(count, CoreMatchers.is(CoreMatchers.equalTo(0L)));
    }

    private void createNArchivedGoals(Profile profile, int n) {
        for (int i = 0; i < n; i++) {
            Goal goal = newGoal(i);
            goal.setArchived(true);
            goalJPAService.save(goal, profile);
        }
    }

    void createNActiveGoals(Profile profile, int n) {
        for (int i = 0; i < n; i++) {
            goalJPAService.save(newGoal(i), profile);
        }
    }

    Goal newGoal(int i) {
        Goal goal = new Goal();
        goal.setDistance(100 + i);
        goal.setGoalUnit(GoalUnit.MI);
        goal.setPurpose("Run" + 100 + i + "miles");
        goal.setGoalType(GoalType.DISTANCE_GOAL);
        return goal;
    }

    private Profile createProfile() {
        Profile profile = Profile.createProfile("test@test.com", "test_user", "Test User", "city", "country", Gender.MALE);
        return profileService.save(profile);
    }

}
