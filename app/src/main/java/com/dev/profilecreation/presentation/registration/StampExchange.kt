class StampExchange {

    // Function to calculate the stamp exchange between Jane and Alice
    fun exchange(janeStamps: IntArray, aliceStamps: IntArray): Pair<List<Int>, List<Int>> {
        // Use maps to count occurrences of each stamp in Jane's and Alice's collections
        val janeCounts = mutableMapOf<Int, Int>()
        val aliceCounts = mutableMapOf<Int, Int>()

        // Iterate over Jane's stamps, incrementing the count for each stamp in the map
        for (stamp in janeStamps) {
            janeCounts[stamp] = janeCounts.getOrDefault(stamp, 0) + 1
        }

        // Do the same for Alice's stamps
        for (stamp in aliceStamps) {
            aliceCounts[stamp] = aliceCounts.getOrDefault(stamp, 0) + 1
        }

        // Create empty lists to store the stamps that Jane and Alice will receive
        val janeNewStamps = mutableListOf<Int>()
        val aliceNewStamps = mutableListOf<Int>()

        // For each stamp in Jane's collection,
        janeCounts.forEach { (stamp, count) ->
            // if she has more than 2 of it and Alice has fewer than 2,
            if (count > 2 && aliceCounts.getOrElse(stamp) { 0 } < 2) {
                // give enough of the stamp to Alice so that Jane still has 2 left.
                repeat(count - 2) {
                    aliceNewStamps.add(stamp)
                }
            }
        }

        // Do the same for Alice's stamps,
        aliceCounts.forEach { (stamp, count) ->
            // if she has more than 2 of a stamp and Jane has fewer than 2,
            if (count > 2 && janeCounts.getOrElse(stamp) { 0 } < 2) {
                // give enough of the stamp to Jane so that Alice still has 2 left.
                repeat(count - 2) {
                    janeNewStamps.add(stamp)
                }
            }
        }

        // Return the new stamps that Jane and Alice will receive
        return Pair(janeNewStamps, aliceNewStamps)
    }
}