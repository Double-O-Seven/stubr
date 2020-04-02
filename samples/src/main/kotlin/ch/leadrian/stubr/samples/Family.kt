package ch.leadrian.stubr.samples

data class Family(
        val mom: Person?,
        val dad: Person?,
        val kids: MutableList<Person>
)