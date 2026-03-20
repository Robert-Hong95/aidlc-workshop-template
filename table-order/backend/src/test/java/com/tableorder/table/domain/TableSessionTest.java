package com.tableorder.table.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TableSessionTest {

    @Test
    void start_새세션_isActive_true() {
        TableSession session = TableSession.start(1L);

        assertThat(session.getTableId()).isEqualTo(1L);
        assertThat(session.isActive()).isTrue();
        assertThat(session.getStartedAt()).isNotNull();
        assertThat(session.getEndedAt()).isNull();
    }

    @Test
    void end_세션종료_isActive_false() {
        TableSession session = TableSession.start(1L);

        session.end();

        assertThat(session.isActive()).isFalse();
        assertThat(session.getEndedAt()).isNotNull();
    }
}
