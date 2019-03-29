export default (data) => {
  let hexcase = 0;
  let chrsz = 8;

  function hexMd5 (s) {
    return binl2hex(coreMd5(str2binl(s), s.length * chrsz));
  }
  function coreMd5 (x, len) {
    x[ len >> 5 ] |= 0x80 << ((len) % 32);
    x[ (((len + 64) >>> 9) << 4) + 14 ] = len;
    let a = 1732584193;
    let b = -271733879;
    let c = -1732584194;
    let d = 271733878;
    for (let i = 0; i < x.length; i += 16) {
      let olda = a;
      let oldb = b;
      let oldc = c;
      let oldd = d;
      a = md5FF(a, b, c, d, x[ i + 0 ], 7, -680876936);
      d = md5FF(d, a, b, c, x[ i + 1 ], 12, -389564586);
      c = md5FF(c, d, a, b, x[ i + 2 ], 17, 606105819);
      b = md5FF(b, c, d, a, x[ i + 3 ], 22, -1044525330);
      a = md5FF(a, b, c, d, x[ i + 4 ], 7, -176418897);
      d = md5FF(d, a, b, c, x[ i + 5 ], 12, 1200080426);
      c = md5FF(c, d, a, b, x[ i + 6 ], 17, -1473231341);
      b = md5FF(b, c, d, a, x[ i + 7 ], 22, -45705983);
      a = md5FF(a, b, c, d, x[ i + 8 ], 7, 1770035416);
      d = md5FF(d, a, b, c, x[ i + 9 ], 12, -1958414417);
      c = md5FF(c, d, a, b, x[ i + 10 ], 17, -42063);
      b = md5FF(b, c, d, a, x[ i + 11 ], 22, -1990404162);
      a = md5FF(a, b, c, d, x[ i + 12 ], 7, 1804603682);
      d = md5FF(d, a, b, c, x[ i + 13 ], 12, -40341101);
      c = md5FF(c, d, a, b, x[ i + 14 ], 17, -1502002290);
      b = md5FF(b, c, d, a, x[ i + 15 ], 22, 1236535329);
      a = md5GG(a, b, c, d, x[ i + 1 ], 5, -165796510);
      d = md5GG(d, a, b, c, x[ i + 6 ], 9, -1069501632);
      c = md5GG(c, d, a, b, x[ i + 11 ], 14, 643717713);
      b = md5GG(b, c, d, a, x[ i + 0 ], 20, -373897302);
      a = md5GG(a, b, c, d, x[ i + 5 ], 5, -701558691);
      d = md5GG(d, a, b, c, x[ i + 10 ], 9, 38016083);
      c = md5GG(c, d, a, b, x[ i + 15 ], 14, -660478335);
      b = md5GG(b, c, d, a, x[ i + 4 ], 20, -405537848);
      a = md5GG(a, b, c, d, x[ i + 9 ], 5, 568446438);
      d = md5GG(d, a, b, c, x[ i + 14 ], 9, -1019803690);
      c = md5GG(c, d, a, b, x[ i + 3 ], 14, -187363961);
      b = md5GG(b, c, d, a, x[ i + 8 ], 20, 1163531501);
      a = md5GG(a, b, c, d, x[ i + 13 ], 5, -1444681467);
      d = md5GG(d, a, b, c, x[ i + 2 ], 9, -51403784);
      c = md5GG(c, d, a, b, x[ i + 7 ], 14, 1735328473);
      b = md5GG(b, c, d, a, x[ i + 12 ], 20, -1926607734);
      a = md5HH(a, b, c, d, x[ i + 5 ], 4, -378558);
      d = md5HH(d, a, b, c, x[ i + 8 ], 11, -2022574463);
      c = md5HH(c, d, a, b, x[ i + 11 ], 16, 1839030562);
      b = md5HH(b, c, d, a, x[ i + 14 ], 23, -35309556);
      a = md5HH(a, b, c, d, x[ i + 1 ], 4, -1530992060);
      d = md5HH(d, a, b, c, x[ i + 4 ], 11, 1272893353);
      c = md5HH(c, d, a, b, x[ i + 7 ], 16, -155497632);
      b = md5HH(b, c, d, a, x[ i + 10 ], 23, -1094730640);
      a = md5HH(a, b, c, d, x[ i + 13 ], 4, 681279174);
      d = md5HH(d, a, b, c, x[ i + 0 ], 11, -358537222);
      c = md5HH(c, d, a, b, x[ i + 3 ], 16, -722521979);
      b = md5HH(b, c, d, a, x[ i + 6 ], 23, 76029189);
      a = md5HH(a, b, c, d, x[ i + 9 ], 4, -640364487);
      d = md5HH(d, a, b, c, x[ i + 12 ], 11, -421815835);
      c = md5HH(c, d, a, b, x[ i + 15 ], 16, 530742520);
      b = md5HH(b, c, d, a, x[ i + 2 ], 23, -995338651);
      a = md5II(a, b, c, d, x[ i + 0 ], 6, -198630844);
      d = md5II(d, a, b, c, x[ i + 7 ], 10, 1126891415);
      c = md5II(c, d, a, b, x[ i + 14 ], 15, -1416354905);
      b = md5II(b, c, d, a, x[ i + 5 ], 21, -57434055);
      a = md5II(a, b, c, d, x[ i + 12 ], 6, 1700485571);
      d = md5II(d, a, b, c, x[ i + 3 ], 10, -1894986606);
      c = md5II(c, d, a, b, x[ i + 10 ], 15, -1051523);
      b = md5II(b, c, d, a, x[ i + 1 ], 21, -2054922799);
      a = md5II(a, b, c, d, x[ i + 8 ], 6, 1873313359);
      d = md5II(d, a, b, c, x[ i + 15 ], 10, -30611744);
      c = md5II(c, d, a, b, x[ i + 6 ], 15, -1560198380);
      b = md5II(b, c, d, a, x[ i + 13 ], 21, 1309151649);
      a = md5II(a, b, c, d, x[ i + 4 ], 6, -145523070);
      d = md5II(d, a, b, c, x[ i + 11 ], 10, -1120210379);
      c = md5II(c, d, a, b, x[ i + 2 ], 15, 718787259);
      b = md5II(b, c, d, a, x[ i + 9 ], 21, -343485551);
      a = saveAdd(a, olda);
      b = saveAdd(b, oldb);
      c = saveAdd(c, oldc);
      d = saveAdd(d, oldd);
    }
    return [ a, b, c, d ];
  }

  function md5Cmn (q, a, b, x, s, t) {
    return saveAdd(bitRol(saveAdd(saveAdd(a, q), saveAdd(x, t)), s), b);
  }

  function md5FF (a, b, c, d, x, s, t) {
    return md5Cmn((b & c) | ((~b) & d), a, b, x, s, t);
  }

  function md5GG (a, b, c, d, x, s, t) {
    return md5Cmn((b & d) | (c & (~d)), a, b, x, s, t);
  }

  function md5HH (a, b, c, d, x, s, t) {
    return md5Cmn(b ^ c ^ d, a, b, x, s, t);
  }

  function md5II (a, b, c, d, x, s, t) {
    return md5Cmn(c ^ (b | (~d)), a, b, x, s, t);
  }

  function saveAdd (x, y) {
    let lsw = (x & 0xFFFF) + (y & 0xFFFF);
    let msw = (x >> 16) + (y >> 16) + (lsw >> 16);
    return (msw << 16) | (lsw & 0xFFFF);
  }

  function bitRol (num, cnt) {
    return (num << cnt) | (num >>> (32 - cnt));
  }

  function str2binl (str) {
    let bin = [];
    let mask = (1 << chrsz) - 1;
    for (let i = 0; i < str.length * chrsz; i += chrsz) bin[ i >> 5 ] |= (str.charCodeAt(i / chrsz) & mask) << (i % 32);
    return bin;
  }

  function binl2hex (binarray) {
    let hexTab = hexcase ? '0123456789ABCDEF' : '0123456789abcdef';
    let str = '';
    for (let i = 0; i < binarray.length * 4; i++) {
      str += hexTab.charAt((binarray[ i >> 2 ] >> ((i % 4) * 8 + 4)) & 0xF) + hexTab.charAt((binarray[ i >> 2 ] >> ((i % 4) * 8)) & 0xF);
    }
    return str;
  }

  return hexMd5(data);
};
