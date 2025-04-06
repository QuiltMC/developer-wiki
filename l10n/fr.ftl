application-title = Wiki pour Développeur·euse·s Quilt

## Locales

en = Anglais
fr = Français

## Sidebar

menu = Menu
articles = Articles

## Lang Dropdown

language = Langue

## Notices

# $wiki_source (String) - The url of the wiki's repo
dev-notice = Attention : Ce site est encore en cours de développement. Veuillez signaler les problèmes (en anglais) ici : <a data-l10n-name="link">{ $wiki_source }</a>.
translation-notice = Veuillez noter que la langue originale de ce wiki est l'anglais et que les versions traduites peuvent ne pas être à jour en comparaison à la <a data-l10n-name="link">version anglaise</a>.
# $wiki_source (String) - The url of the wiki's repo
draft-notice = Cet article est prévu mais n'a pas encore été implémentée. Vous pouvez participer à la création de ce wiki ici : <a data-l10n-name="link">{ $wiki_source }</a>.
# $current_locale (String) - The translated name of the current selected locale
# $fallback_locale (String) - The translated name of the selected fallback locale
article-not-translated-notice = Cet article n'est pas encore traduit en { $current_locale }, mais nous avons trouvé une version en { $fallback_locale }.

## Errors

# $error_code (String) - The status code of the error
error-title = Erreur { $error_code }
# $page_path (String) - The path to the page, without the locale
not-found-error = Aucune page trouvée à l'adresse <code>{ $page_path }</code>.
# $article_path (String) - The path to the article, without the locale
article-not-found-error = Aucun article trouvé à l'adresse <code>{ $article_path }</code>.
server-error = Le server a rencontré une erreur, veuillez réessayer.

## Wiki

# Blocks
blocks = Blocs
    .first-block = Ajouter un Bloc Simple
    .oriented-block = Ajouter un Bloc Orienté
    .redstone-interaction = Ajouter des fonctionnalités Redstone à votre Bloc

# Concepts
concepts = Concepts
    .events = Événements
    .libraries = Liste de Librairies Tiers
    .lifecycles-ticks = Cycles de Vie et Ticks
    .minecraft-code-structure = Structure du Code de Minecraft
    .mixins = Mixins
    .nbt = NBT et Données dans Minecaft
    .networking = Réseau
    .qsl-qfapi = Aperçu des QSL et de la QFAPI
    .registries = Registres
    .sideness = Côté Serveur et Côté Client

# Data
data = Données
    .adding-recipes = Ajouter des Recettes
    .rea = Utiliser le "Registry Entry Attachments" (REA)
    .recipe-api = API des Recettes
    .recipe-type = Ajouter un Type de Recette
    .resource-loader = "Resource Loader"

# Introduction
introduction = Introduction
    .getting-started = Débuter avec Quilt
    .setting-up = Mettre en place un Environement de Développement

# Items
items = Items
    .armor = Ajouter une Armure
    .first-item = Créer votre Premier Item
    .food = Ajouter de la Nourriture
    .tools = Ajouter des Outils Personnalisés

# Configuration
configuration = Configuration
    .getting-started = Débuter avec Quilt Config
    .advanced-configuring = Configuration Avancée
    .config-screen = Mettre en place un Écran de Configuration
    .metadata = Référence pour les Annotations et les Métadonnées

# Misc
misc = Divers
    .commands = Ajouter des Commandes
    .sounds = Ajouter des Sons
    .mappings = Personnaliser vos Mappings
    .world_types = Ajouter des Types de Monde
