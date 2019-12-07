package pl.edu.agh.kis.android.ships.components

class Score {
    var scoreValue: Int = 0
    var userName: String? = null
    constructor(scoreValue: Int, userName: String) {
        this.scoreValue = scoreValue
        this.userName = userName
    }
}