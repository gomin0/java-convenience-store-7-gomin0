package store.domain.membership;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class MembershipTest {

    @DisplayName("멤버십이 활성화되면 할인이 적용된다")
    @Test
    void calculateDiscountWithActivatedMembership() {
        Membership membership = Membership.from(true);
        assertThat(membership.calculateDiscount(10000)).isEqualTo(3000);
    }

    @DisplayName("멤버십이 비활성화되면 할인이 적용되지 않는다")
    @Test
    void calculateDiscountWithDeactivatedMembership() {
        Membership membership = Membership.from(false);
        assertThat(membership.calculateDiscount(10000)).isZero();
    }

    @DisplayName("멤버십 할인은 8000원을 초과할 수 없다")
    @Test
    void calculateDiscountWithMaxLimit() {
        Membership membership = Membership.from(true);
        assertThat(membership.calculateDiscount(30000)).isEqualTo(8000);
    }

    @DisplayName("멤버십 할인은 금액의 30%가 적용된다")
    @ParameterizedTest
    @CsvSource({
            "10000, 3000",
            "20000, 6000",
            "5000, 1500",
            "1000, 300"
    })
    void calculateDiscount(int amount, int expected) {
        Membership membership = Membership.from(true);
        assertThat(membership.calculateDiscount(amount)).isEqualTo(expected);
    }
}