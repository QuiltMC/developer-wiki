<nav class="navbar is-dark" role="navigation" aria-label="main navigation" data-controller="navbar">
    <div class="navbar-brand">
        <a class="navbar-item pr-6" href="/">
            <img src="https://quiltmc.org/assets/img/logo.svg" alt="Quilt Logo">

            <span class="ml-2">DevWiki</span>
        </a>

        <a class="navbar-item px-2 has-tooltip-arrow has-tooltip-bottom" style="display: none" id="language-switcher" data-tooltip="Switch Language">
            <i class="fas fa-language fa-2xl"></i>
        </a>

        <a class="navbar-item px-2 has-tooltip-arrow has-tooltip-bottom" id="theme-switcher" data-tooltip="Toggle Low Contrast">
            <span id="theme-normal-contrast">
                <i class="fas fa-circle-half-stroke fa-xl"></i>
            </span>

            <span id="theme-low-contrast">
                <i class="fal fa-circle-half-stroke fa-xl"></i>
            </span>
        </a>

        <a class="navbar-item px-2 has-tooltip-arrow has-tooltip-bottom" href="https://quiltmc.org/" data-tooltip="Website Home">
            <span><i class="fas fa-home fa-lg"></i></span>
        </a>

        <a role="button" data-navbar-target="menu" data-action="click->navbar#toggle" class="navbar-burger" aria-label="menu" aria-expanded="false">
            <span aria-hidden="true"></span>
            <span aria-hidden="true"></span>
            <span aria-hidden="true"></span>
        </a>
    </div>

    <div class="navbar-menu" data-navbar-target="menu">
        <div class="navbar-end">
            <a href="https://github.com/QuiltMC/developer-wiki" class="navbar-item has-icon" target="_blank">
                <span class="icon"><i class="fab fa-github fa-xl"></i></span>
                <span class="is-hidden-desktop">GitHub</span>
            </a>
        </div>
    </div>
</nav>
