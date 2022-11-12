package lotto.domain;

import lotto.constant.Rank;

import java.util.*;

public class LottoPurchaseInformation {
    private static final int THOUSAND = 1000;
    private ValidationInDomain validator = new ValidationInDomain();
    private List<Lotto> lottoTickets = new ArrayList<>();
    private LottoGenerator lottoGenerator = new LottoGenerator();
    private Map<Rank, Integer> winningStatistics = new EnumMap<>(Rank.class);
    private int numberOfTickets;
    private int purchaseAmount;
    private int totalWinningAmount;
    private double rateOfReturn;


    public LottoPurchaseInformation(int purchaseAmount){
        validator.checkDividedByThousand(purchaseAmount);
        this.purchaseAmount = purchaseAmount;
        this.numberOfTickets = this.purchaseAmount/THOUSAND;
    }
    public List<Lotto> getLottoTickets(){
        return lottoTickets;
    }

    public void generateLottoTickets(){
        for(int i = 0; i < numberOfTickets; i++){
            Lotto lotto = new Lotto(lottoGenerator.generateLottoNumbers());
            lottoTickets.add(lotto);
        }
    }

    private void initializeWinningStatistics(){
        winningStatistics.put(Rank.FIFTH, 0);
        winningStatistics.put(Rank.FOURTH, 0);
        winningStatistics.put(Rank.THIRD, 0);
        winningStatistics.put(Rank.SECOND, 0);
        winningStatistics.put(Rank.FIRST, 0);
    }

    public void compareLottoTicketsWith(List<Integer> winningNumbers, int bonusNumber){
        initializeWinningStatistics();
        for(int i = 0; i < lottoTickets.size(); i++) {
            Lotto lotto = lottoTickets.get(i);
            int count = lotto.calculateCountOfMatchingNumbers(winningNumbers);
            String winningData = count + "개 일치";
            if(count == 5 && lotto.contains(bonusNumber)){
                winningData += " 보너스 볼 일치";
            }
            renewWinningStatistics(winningData);
        }
    }

    private void renewWinningStatistics(String winningData){
        for(int i =  0; i < Rank.size(); i++) {
            Rank winningRank = Rank.get(i);
            if (winningData.equals(winningRank.condition())) {
                winningStatistics.replace(winningRank, winningStatistics.get(winningRank) + 1);
                return;
            }
        }
    }

    public Map<Rank, Integer> getWinningStatistics(){
        return winningStatistics;
    }

    public void calculateTotalWinningAmount(){
        totalWinningAmount = 0;
        for(int i = 0; i < 5; i++){
            totalWinningAmount += winningStatistics.get(Rank.get(i)) * Rank.get(i).winningAmount();
        }
    }

    public void calculateRateOfReturn(){
        double tmp = ((double)totalWinningAmount/purchaseAmount)*1000;
        rateOfReturn = Math.round(tmp)/10.0;
    }

    public double getRateOfReturn(){
        return rateOfReturn;
    }



}
