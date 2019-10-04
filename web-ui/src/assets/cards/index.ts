export enum CardValue {
    '7c' = '7c',
    '7d' = '7d',
    '7h' = '7h',
    '7s' = '7s',
    '8c' = '8c',
    '8d' = '8d',
    '8h' = '8h',
    '8s' = '8s',
    '9c' = '9c',
    '9d' = '9d',
    '9h' = '9h',
    '9s' = '9s',
    tc = 'tc',
    td = 'td',
    th = 'th',
    ts = 'ts',
    jc = 'jc',
    jd = 'jd',
    jh = 'jh',
    js = 'js',
    qc = 'qc',
    qd = 'qd',
    qh = 'qh',
    qs = 'qs',
    kc = 'kc',
    kd = 'kd',
    kh = 'kh',
    ks = 'ks',
    ac = 'ac',
    ad = 'ad',
    ah = 'ah',
    as = 'as',
    blue_back = 'blue_back',
    gray_back = 'gray_back',
    green_back = 'green_back',
    purple_back = 'purple_back',
    red_back = 'red_back',
    yellow_back = 'yellow_back',
    card_placeholder = 'card_placeholder',
}

const cards = {
  [CardValue['7c']]: require('./7C.png'),
  [CardValue['7d']]: require('./7D.png'),
  [CardValue['7h']]: require('./7H.png'),
  [CardValue['7s']]: require('./7S.png'),
  [CardValue['8c']]: require('./8C.png'),
  [CardValue['8d']]: require('./8D.png'),
  [CardValue['8h']]: require('./8H.png'),
  [CardValue['8s']]: require('./8S.png'),
  [CardValue['9c']]: require('./9C.png'),
  [CardValue['9d']]: require('./9D.png'),
  [CardValue['9h']]: require('./9H.png'),
  [CardValue['9s']]: require('./9S.png'),
  [CardValue['tc']]: require('./10C.png'),
  [CardValue['td']]: require('./10D.png'),
  [CardValue['th']]: require('./10H.png'),
  [CardValue['ts']]: require('./10S.png'),
  [CardValue['jc']]: require('./JC.png'),
  [CardValue['jd']]: require('./JD.png'),
  [CardValue['jh']]: require('./JH.png'),
  [CardValue['js']]: require('./JS.png'),
  [CardValue['qc']]: require('./QC.png'),
  [CardValue['qd']]: require('./QD.png'),
  [CardValue['qh']]: require('./QH.png'),
  [CardValue['qs']]: require('./QS.png'),
  [CardValue['kc']]: require('./KC.png'),
  [CardValue['kd']]: require('./KD.png'),
  [CardValue['kh']]: require('./KH.png'),
  [CardValue['ks']]: require('./KS.png'),
  [CardValue['ac']]: require('./AC.png'),
  [CardValue['ad']]: require('./AD.png'),
  [CardValue['ah']]: require('./AH.png'),
  [CardValue['as']]: require('./AS.png'),
  [CardValue.blue_back]: require('./blue_back.png'),
  [CardValue.gray_back]: require('./gray_back.png'),
  [CardValue.green_back]: require('./green_back.png'),
  [CardValue.purple_back]: require('./purple_back.png'),
  [CardValue.red_back]: require('./red_back.png'),
  [CardValue.yellow_back]: require('./yellow_back.png'),
  [CardValue.card_placeholder]: require('./card_placeholder.png'),
};

export default cards;