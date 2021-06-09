import os

ROOT_PATH = os.path.dirname(os.path.dirname(__file__))
DATA_PATH = os.path.join(ROOT_PATH, "../data")
LARGE_SWT_PATH = os.path.join(DATA_PATH, "SWT-large")
LARGE_SOURCE_CODE_PATH = os.path.join(LARGE_SWT_PATH, "swt-source-code")
LARGE_XML_FILE_PATH = os.path.join(LARGE_SWT_PATH, "SWTBugRepository.xml")

NORMAL_SWT_PATH = os.path.join(DATA_PATH, "SWT-normal")
NORMAL_SOURCE_CODE_PATH = os.path.join(NORMAL_SWT_PATH, "swt-source-code")
NORMAL_XML_FILE_PATH = os.path.join(NORMAL_SWT_PATH, "SWTBugRepository.xml")

SCORES_NEEDED_TO_LEARN = ["token_match", "calculate_vsm_similarity"]
SCORES_NEEDED_TO_CALCULATE = ["token_match", "calculate_vsm_similarity"]

# for deploy
# DEPLOY_DATA_PATH = '/data/'
DEPLOY_DATA_PATH = 'E:\\4_work_dir\\'
# for dev
DEV_DATA_PATH = ''

STOP_WORDS = set(
    ['i', 'me', 'my', 'myself', 'we', 'our', 'ours', 'ourselves', 'you', 'your',
     'yours', 'yourself', 'yourselves', 'he', 'him', 'his', 'himself', 'she', 'her',
     'hers', 'herself', 'it', 'its', 'itself', 'they', 'them', 'their', 'theirs',
     'themselves', 'what', 'which', 'who', 'whom', 'this', 'that', 'these', 'those',
     'am', 'is', 'are', 'was', 'were', 'be', 'been', 'being', 'have', 'has', 'had',
     'having', 'do', 'does', 'did', 'doing', 'a', 'an', 'the', 'and', 'but', 'if',
     'or', 'because', 'as', 'until', 'while', 'of', 'at', 'by', 'for', 'with', 'about',
     'against', 'between', 'into', 'through', 'during', 'before', 'after', 'above',
     'below', 'to', 'from', 'up', 'down', 'in', 'out', 'on', 'off', 'over', 'under',
     'again', 'further', 'then', 'once', 'here', 'there', 'when', 'where', 'why',
     'how', 'all', 'any', 'both', 'each', 'few', 'more', 'most', 'other', 'some',
     'such', 'no', 'nor', 'not', 'only', 'own', 'same', 'so', 'than', 'too', 'very',
     's', 't', 'can', 'will', 'just', 'don', 'should', 'now', 'd', 'll', 'm', 'o',
     're', 've', 'y', 'ain', 'aren', 'couldn', 'didn', 'doesn', 'hadn', 'hasn', 'haven',
     'isn', 'ma', 'mightn', 'mustn', 'needn', 'shan', 'shouldn', 'wasn', 'weren', 'won',
     'wouldn', 'b', 'c', 'e', 'f', 'g', 'h', 'j', 'k', 'l', 'n', 'p', 'q', 'u', 'v',
     'w', 'x', 'z', 'us'])

JAVA_KEYWORDS = set(
    ['abstract', 'assert', 'boolean', 'break', 'byte', 'case',
     'catch', 'char', 'class', 'const', 'continue', 'default', 'do', 'double',
     'else', 'enum', 'extends', 'false', 'final', 'finally', 'float', 'for', 'goto',
     'if', 'implements', 'import', 'instanceof', 'int', 'interface', 'long',
     'native', 'new', 'null', 'package', 'private', 'protected', 'public', 'return',
     'short', 'static', 'strictfp', 'super', 'switch', 'synchronized', 'this',
     'throw', 'throws', 'transient', 'true', 'try', 'void', 'volatile', 'while'])
