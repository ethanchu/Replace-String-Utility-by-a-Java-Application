Since this application was keeping track of tournament detailed status and each players' prize changes.

To realize this requirement, I added to the design a class tournament manager with attribute of "TournamentStatus" to keep track of which view operations were used(ViewMatchlist(), ViewPlayerlistbyTotal and ViewPlayer). For "tournament manager". Operation(StartTournament) passed the house cut, entry price, player username, tournament status and tournament ID to instantiate/start the tournament. EndTournament was used to stop tournament by force and made refunds the prices to players. Addplayer and RemoverPlayer changed the players in PlayerList. Startmatch and Endmatch controled each match under tournament by tournament manager. ViewMatchlist, ViewPlayerlistbyTotal and ViewPlayer had an associated relation with matchlist, Playerlist and Player seperately, and showed up based on TournamentStatus's value.

"tournament" class built by the class of "tournament manager". And there was a associated class "StartTournament" with "tournament manager". When "tournament" was built, "StartTournament" showed "Playername" and "PotentialPrizeandProfit".

Under "tournament" class, the included attributes were hourse cut, entry price, username list, tournamentstatus, tournament ID. and two derived attributes prize and profit
when it was created, it created its own matchlist by BuildMatch, and letted the matchlist handle the list of matches. And tournament got the result from matchlist by GetWinnerList(). Based on tournament status, tournament interacted with player by different associated relation of "refund" and "SendoutPrize".

"matchlist" class had attributes of usernamelist, PairedPlayerlist, WinnerList and MatchstatusList to keep the record of each match. And had operations of "BuildMatch", "GetWinner" and "Getmatchstatus" to interact with match class.

"match" class holded attributes of matchID, two players' username by Playerlist, winner and matchstatus. It was triggerred to start/end by tournament manager.

Another one key class "Player", it owned name, unique username, phone number and deckchoice as attributes. It also have a attribute of "TournamentStatus" to decide the display operation(ViewMatchlist and ViewPlayerlistbyTotal) to be shown. Two derived attributes(IndividualPrize and TotalPrize) can be calculated from tournament by Refund and SendoutPrize relation.

"PlayerList" class will hold array of "Player" as its attribute, so "PlayerList" has an aggregation relation with Player. "PlayerList" was used by "Player" and "tournament manager" which need to show playerlist sorted by total.

TournamentStatus and MatchStatus were designed by Enummeration because I assumed that they are a set of ways to be selected: (i.e. start, pending, completed, end).



