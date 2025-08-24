package org.stlm.game.button.buttongame.utils

val EMOJI: List<String> = listOf(
    "&#128512;", // 😀 Grinning Face
    "&#128515;", // 😃 Grinning Face with Big Eyes
    "&#128516;", // 😄 Grinning Face with Smiling Eyes
    "&#128513;", // 😁 Beaming Face with Smiling Eyes
    "&#128514;", // 😂 Face with Tears of Joy
    "&#128517;", // 😅 Rolling on the Floor Laughing
    "&#128518;", // 😆 Grinning Squinting Face
    "&#128519;", // 😇 Smiling Face with Halo
    "&#128520;", // 😊 Smiling Face with Smiling Eyes
    "&#128521;", // 😋 Face Savoring Food
    "&#128522;", // 😌 Relieved Face
    "&#128523;", // 😍 Smiling Face with Heart-Eyes
    "&#128524;", // 😎 Smiling Face with Sunglasses
    "&#128525;", // 😏 Smirking Face
    "&#128526;", // 😐 Neutral Face
    "&#128527;", // 😑 Expressionless Face
    "&#128528;", // 😒 Unamused Face
    "&#128529;", // 😓 Downcast Face with Sweat
    "&#128530;", // 😔 Pensive Face
    "&#128531;", // 😕 Confused Face
    "&#128532;", // 😖 Confounded Face
    "&#128533;", // 😗 Kissing Face
    "&#128534;", // 😘 Face Blowing a Kiss
    "&#128535;", // 😙 Kissing Face with Smiling Eyes
    "&#128536;", // 😚 Kissing Face with Closed Eyes
    "&#128537;", // 😛 Face with Tongue
    "&#128538;", // 😜 Winking Face with Tongue
    "&#128539;", // 😝 Squinting Face with Tongue
    "&#128540;", // 😞 Disappointed Face
    "&#128541;", // 😟 Worried Face
    "&#128542;", // 😠 Angry Face
    "&#128543;", // 😡 Pouting Face
    "&#128544;", // 😢 Crying Face
    "&#128545;", // 😣 Persevering Face
    "&#128546;", // 😤 Face with Steam From Nose
    "&#128547;", // 😥 Sad but Relieved Face
    "&#128548;", // 😦 Frowning Face with Open Mouth
    "&#128549;", // 😧 Anguished Face
    "&#128550;", // 😨 Fearful Face
    "&#128551;", // 😩 Weary Face
    "&#128552;", // 😪 Sleepy Face
    "&#128553;", // 😫 Tired Face
    "&#128554;", // 😬 Grimacing Face
    "&#128555;", // 😭 Loudly Crying Face
    "&#128556;", // 😮 Face with Open Mouth
    "&#128557;", // 😯 Hushed Face
    "&#128558;", // 😰 Anxious Face with Sweat
    "&#128559;", // 😱 Face Screaming in Fear
    "&#128560;", // 😲 Astonished Face
    "&#128561;", // 😳 Flushed Face
    "&#128562;", // 😴 Sleeping Face
    "&#128563;", // 😵 Dizzy Face
    "&#128564;", // 😶 Face Without Mouth
    "&#128565;", // 😷 Face with Medical Mask
    "&#128566;", // 😸 Grinning Cat with Smiling Eyes
    "&#128567;", // 😹 Cat with Tears of Joy
    "&#128568;", // 😺 Grinning Cat
    "&#128569;", // 😻 Smiling Cat with Heart-Eyes
    "&#128570;", // 😼 Cat with Wry Smile
    "&#128571;", // 😽 Kissing Cat
    "&#128572;", // 😾 Pouting Cat
    "&#128573;", // 😿 Crying Cat
    "&#128574;", // 🙀 Weary Cat
    "&#128575;", // 🙁 Slightly Frowning Face
    "&#128576;", // 🙂 Slightly Smiling Face
    "&#128577;", // 🙃 Upside-Down Face
    "&#128578;", // 🙄 Face with Rolling Eyes
    "&#128579;", // 🙅 Person Gesturing No
    "&#128580;", // 🙆 Person Gesturing OK
    "&#128581;", // 🙇 Person Bowing
    "&#128582;", // 🙈 See-No-Evil Monkey
    "&#128583;", // 🙉 Hear-No-Evil Monkey
    "&#128584;", // 🙊 Speak-No-Evil Monkey
    "&#128585;", // 🙋 Happy Person Raising One Hand
    "&#128586;", // 🙌 Person Raising Both Hands in Celebration
    "&#128587;", // 🙍 Person Frowning
    "&#128588;", // 🙎 Person Pouting
    "&#128589;", // 🙏 Folded Hands
    "&#129296;", // 🤐 Zipper-Mouth Face
    "&#129297;", // 🤑 Money-Mouth Face
    "&#129298;", // 🤒 Face with Thermometer
    "&#129299;", // 🤓 Nerd Face
    "&#129300;", // 🤔 Thinking Face
    "&#129301;", // 🤕 Face with Head-Bandage
    "&#129302;", // 🤖 Robot Face
    "&#129303;", // 🤗 Hugging Face
    "&#129304;", // 🤘 Sign of the Horns
    "&#129305;", // 🤙 Call Me Hand
    "&#129306;", // 🤚 Raised Back of Hand
    "&#129307;", // 🤛 Left-Facing Fist
    "&#129308;", // 🤜 Right-Facing Fist
    "&#129309;", // 🤝 Handshake
    "&#129310;", // 🤞 Crossed Fingers
    "&#129311;", // 🤟 Love-You Gesture
    "&#129312;", // 🤠 Cowboy Hat Face
    "&#129313;", // 🤡 Clown Face
    "&#129314;", // 🤢 Nauseated Face
    "&#129315;", // 🤣 Rolling on the Floor Laughing
    "&#129316;", // 🤤 Drooling Face
    "&#129317;", // 🤥 Lying Face
    "&#129318;", // 🤦 Person Facepalming
    "&#129319;", // 🤧 Sneezing Face
    "&#129320;", // 🤨 Face with Raised Eyebrow
    "&#129321;", // 🤩 Star-Struck
    "&#129322;", // 🤪 Zany Face
    "&#129323;", // 🤫 Shushing Face
    "&#129324;", // 🤬 Face with Symbols on Mouth
    "&#129325;", // 🤭 Face with Hand Over Mouth
    "&#129326;", // 🤮 Face Vomiting
    "&#129327;", // 🤯 Exploding Head
    "&#129328;", // 🤰 Pregnant Woman
    "&#129329;", // 🤱 Breast-Feeding
    "&#129330;", // 🤲 Palms Up Together
    "&#129331;", // 🤳 Selfie
    "&#129332;", // 🤴 Prince
    "&#129333;", // 🤵 Person in Tuxedo
    "&#129334;", // 🤶 Mrs. Claus
    "&#129335;", // 🤷 Person Shrugging
    "&#129336;", // 🤸 Person Cartwheeling
    "&#129337;", // 🤹 Person Juggling
    "&#129338;", // 🤺 Person Fencing
    "&#129340;", // 🤼 People Wrestling
    "&#129341;", // 🤽 Person Playing Water Polo
    "&#129342;", // 🤾 Person Playing Handball
    "&#129343;", // 🤿 Diving Mask
    "&#129344;", // 🥀 Wilted Flower
    "&#129345;", // 🥁 Drum
    "&#129346;", // 🥂 Clinking Glasses
    "&#129347;", // 🥃 Tumbler Glass
    "&#129348;", // 🥄 Spoon
    "&#129349;", // 🥅 Goal Net
    "&#129350;", // 🥇 1st Place Medal
    "&#129351;", // 🥈 2nd Place Medal
    "&#129352;", // 🥉 3rd Place Medal
    "&#129353;", // 🥊 Boxing Glove
    "&#129354;", // 🥋 Martial Arts Uniform
    "&#129355;", // 🥐 Croissant
    "&#129356;", // 🥑 Avocado
)

val CHINESE: List<String> = listOf(
    "&#x597D;",   // 好 (good)
    "&#x55CE;",   // 嗯 (uh-huh, yes)
    "&#x4F60;",   // 你 (you)
    "&#x6211;",   // 我 (I, me)
    "&#x4ED6;",   // 他 (he, him)
    "&#x5979;",   // 她 (she, her)
    "&#x4E2D;",   // 中 (middle, China)
    "&#x56FD;",   // 国 (country)
    "&#x8BED;",   // 语 (language)
    "&#x6587;",   // 文 (writing, culture)
    "&#x5B66;",   // 学 (study, learn)
    "&#x751F;",   // 生 (life, student)
    "&#x5E74;",   // 年 (year)
    "&#x6708;",   // 月 (month, moon)
    "&#x65E5;",   // 日 (day, sun)
    "&#x65F6;",   // 时 (time)
    "&#x95F4;",   // 间 (space, between)
    "&#x91D1;",   // 金 (gold, metal)
    "&#x6728;",   // 木 (wood)
    "&#x6C34;",   // 水 (water)
    "&#x706B;",   // 火 (fire)
    "&#x571F;",   // 土 (earth)
    "&#x7F8E;",   // 美 (beautiful)
    "&#x4E3B;",   // 主 (main, host)
    "&#x8981;",   // 要 (want, important)
    "&#x53BB;",   // 去 (go)
    "&#x6765;",   // 来 (come)
    "&#x770B;",   // 看 (look, see)
    "&#x8BF4;",   // 说 (speak, say)
    "&#x542C;",   // 听 (listen)
    "&#x559C;",   // 喜 (like, happy)
    "&#x6B22;",   // 欢 (joy, pleased)
    "&#x7231;",   // 爱 (love)
    "&#x5BB6;",   // 家 (home, family)
    "&#x4EBA;",   // 人 (person)
    "&#x5927;",   // 大 (big)
    "&#x5C0F;",   // 小 (small)
    "&#x591A;",   // 多 (many)
    "&#x5C11;",   // 少 (few)
    "&#x957F;",   // 长 (long)
    "&#x77ED;",   // 短 (short)
    "&#x9AD8;",   // 高 (tall, high)
    "&#x4F4E;",   // 低 (low)
    "&#x5F00;",   // 开 (open)
    "&#x5173;",   // 关 (close)
    "&#x524D;",   // 前 (front, before)
    "&#x540E;",   // 后 (back, after)
    "&#x5DE6;",   // 左 (left)
    "&#x53F3;",   // 右 (right)
    "&#x4E0A;",   // 上 (up, above)
    "&#x4E0B;",   // 下 (down, below)
    "&#x91CC;",   // 里 (inside)
    "&#x5916;",   // 外 (outside)
    "&#x98CE;",   // 风 (wind)
    "&#x96E8;",   // 雨 (rain)
    "&#x96EA;",   // 雪 (snow)
    "&#x96F7;",   // 雷 (thunder)
    "&#x7535;",   // 电 (electricity)
    "&#x5C71;",   // 山 (mountain)
    "&#x6C34;",   // 水 (water - repeated but common)
    "&#x6CB3;",   // 河 (river)
    "&#x6D77;",   // 海 (sea)
    "&#x5730;",   // 地 (ground, earth)
    "&#x5929;",   // 天 (sky, heaven)
    "&#x98DF;",   // 食 (food, eat)
    "&#x996E;",   // 饮 (drink)
    "&#x7761;",   // 睡 (sleep)
    "&#x9192;",   // 醒 (wake up)
    "&#x8D70;",   // 走 (walk)
    "&#x8DD1;",   // 跑 (run)
)