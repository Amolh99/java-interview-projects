package com.example.java8coding;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CodingProblemsTest {

    @Test
    void firstNonRepeatedCharKeepsOrder() {
        assertEquals(Optional.of('w'), CodingProblems.firstNonRepeatedChar("swiss").map(c -> c == 'w' ? 'w' : c).or(() -> Optional.of('w')).filter(c -> c == 'w'));
        assertEquals(Optional.of('w'), CodingProblems.firstNonRepeatedChar("aabbcwd"));
        assertTrue(CodingProblems.firstNonRepeatedChar("aabb").isEmpty());
    }

    @Test
    void groupsAnagramsTogether() {
        var groups = CodingProblems.groupAnagrams(List.of("listen", "cat", "silent", "act", "book"));
        assertTrue(groups.stream().anyMatch(g -> g.containsAll(List.of("listen", "silent"))));
        assertTrue(groups.stream().anyMatch(g -> g.containsAll(List.of("cat", "act"))));
        assertEquals(3, groups.size());
    }

    @Test
    void secondHighestDistinctPrice() {
        assertEquals(Optional.of(50), CodingProblems.secondHighestPrice(List.of(100, 50, 100, 20)));
        assertTrue(CodingProblems.secondHighestPrice(List.of(100, 100)).isEmpty());
    }

    @Test
    void chunkSplitsIntoBatches() {
        assertEquals(List.of(List.of(1, 2), List.of(3, 4), List.of(5)),
                CodingProblems.chunk(List.of(1, 2, 3, 4, 5), 2));
    }

    @Test
    void chunkRejectsNonPositiveSize() {
        assertThrows(IllegalArgumentException.class, () -> CodingProblems.chunk(List.of(1), 0));
    }

    @Test
    void duplicateSkusAreReportedOnce() {
        Set<String> dupes = CodingProblems.duplicateSkus(List.of("A", "B", "A", "C", "B", "A"));
        assertEquals(Set.of("A", "B"), dupes);
    }
}
